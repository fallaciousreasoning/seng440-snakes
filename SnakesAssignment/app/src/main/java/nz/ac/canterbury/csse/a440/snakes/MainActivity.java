package nz.ac.canterbury.csse.a440.snakes;

//http://stackoverflow.com/questions/11184503/android-emulator-orientation-change-through-emulator-console-or-adb

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.InjectableSensorManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.GameUpdater;
import nz.ac.canterbury.csse.a440.snakes.snake.InputMethod;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeAlmost3DController;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.SnakeDepthRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.SnakeFoodDepthRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.SnakeScoreRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeAccelerometerController;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeButtonController;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeCompassController;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeController;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.SnakeGLView;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeGPSController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.SnakeMinecraftRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.controllers.SnakeSwipeController;
import nz.ac.canterbury.csse.a440.snakes.snake.StartFinishGestureListener;
import nz.ac.canterbury.csse.a440.snakes.snake.renderers.StartFinishRenderer;

public class MainActivity extends AppCompatActivity {
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 1;
    private static final int LOCATION_COARSE_REQUEST = INITIAL_REQUEST + 2;
    private static final int INTERNET_REQUEST = INITIAL_REQUEST + 3;
    SnakeGame game;
    GameUpdater updater;
    //Indicates whether the game should be in 3D
    private boolean is3d;

    private SensorManager sensorManager;

    private LocationManager locationManager;
    private GestureDetectorCompat gestureDetector;
    private AggregateGestureListener gestureListener;
    private StartFinishGestureListener startFinishGestureListener;

    private SnakeSwipeController swipeController;
    private SnakeAccelerometerController accelerometerController;
    private SnakeCompassController compassController;
    private SnakeButtonController buttonController;
    private SnakeGPSController gpsController;
    private SnakeController snakeController;

    private SnakeGLView gameGLRenderer;
    private StartFinishRenderer startFinishRenderer;
    private SnakeScoreRenderer scoreRenderer;
    private SnakeDepthRenderer snakeDepthRenderer;
    private SnakeFoodDepthRenderer snakeFoodDepthRenderer;
    private SnakeMinecraftRenderer minecraftRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //TODO log this somewhere?
                ex.printStackTrace();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!canAccessLocation() || !canAccessLocationCoarse() || !canAccessInternet()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }

        try {
            LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            Location location = locationManager.getLastKnownLocation(provider.getName());
            gpsController = new SnakeGPSController(location);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        accelerometerController = new SnakeAccelerometerController();
        compassController = new SnakeCompassController();

        gameGLRenderer = (SnakeGLView) findViewById(R.id.gameGLRenderer);
        minecraftRenderer = new SnakeMinecraftRenderer(this);

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreRenderer = new SnakeScoreRenderer();
        scoreRenderer.setTextView(scoreText);

        TextView depthText = (TextView)findViewById(R.id.depthText);
        snakeDepthRenderer = new SnakeDepthRenderer();
        snakeDepthRenderer.setTextView(depthText);

        TextView foodDepthText = (TextView)findViewById(R.id.foodDepthText);
        snakeFoodDepthRenderer = new SnakeFoodDepthRenderer();
        snakeFoodDepthRenderer.setTextView(foodDepthText);

        TextView gameStatusText = (TextView) findViewById(R.id.gameStatusText);
        startFinishRenderer = new StartFinishRenderer(getString(R.string.gameStatusStart), getString(R.string.gameStatusReset));
        startFinishRenderer.setTextView(gameStatusText);

        updater = new GameUpdater();

        gestureListener = new AggregateGestureListener();
        gestureDetector = new GestureDetectorCompat(getBaseContext(), gestureListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        minecraftRenderer.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                break;
            case LOCATION_COARSE_REQUEST:
                break;
            case INITIAL_REQUEST:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        game = Util.readGame(this);
        is3d = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("allow_3d_enabled", false);
        gameGLRenderer.setUse3D(is3d);

        if (game == null || game.getBounds().getDepth() == 1 && is3d || game.getBounds().getDepth() != 1 && !is3d) {
            game = new SnakeGame(20, 30, is3d ? 20 : 1, 3);
        }
        game.addRenderer(gameGLRenderer);
        //game.addRenderer(minecraftRenderer);
        game.addRenderer(scoreRenderer);
        game.addRenderer(startFinishRenderer);
        game.addRenderer(snakeDepthRenderer);
        game.addRenderer(snakeFoodDepthRenderer);

        if (updater == null) {
            updater = new GameUpdater();
        }

        if (gestureListener == null) {
            gestureListener = new AggregateGestureListener();
            StartFinishGestureListener startFinishGestureListener = new StartFinishGestureListener();
            startFinishGestureListener.setGame(game);
            gestureListener.addGestureListener(startFinishGestureListener);
        }
        if (gestureDetector == null) {
            gestureDetector = new GestureDetectorCompat(getBaseContext(), gestureListener);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        Util.writeGame(this, game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.render();

        updater.setGame(game);

        // register this class as a listener for the orientation and
        // accelerometer sensors
        SnakeApplication app = (SnakeApplication) this.getApplication();

        //Check if we should enable the sensor injector
        boolean sensor_injector_enabled = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("sensor_injector_enabled", false);
        InjectableSensorManager.setUseSystem(!sensor_injector_enabled);
        sensorManager = app.ism;
        if (sensorManager != null) {
            InjectableSensorManager ism = (InjectableSensorManager) sensorManager;
            //Get the ip for the sensor injector
            String ip = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString("sensor_injector_ip", "127.0.0.1");
            ism.createRemoteListener(ip, 51234);
        }

        assert sensorManager != null;
        sensorManager.registerListener(accelerometerController,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassController,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassController,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        //Setup the controls
        setupControls();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, (float) 0.1, gpsController);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        //Set the update rate
        String speedString = PreferenceManager.getDefaultSharedPreferences(this).getString("game_speed", "1");
        updater.setUpdateRate((int) (1000 / Float.parseFloat(speedString)));

        gameGLRenderer.onResume();
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(accelerometerController);
        sensorManager.unregisterListener(compassController);
        try {
            locationManager.removeUpdates(gpsController);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        gameGLRenderer.onPause();
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessLocationCoarse() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean canAccessInternet() {
        return (hasPermission(Manifest.permission.INTERNET));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.gestureDetector != null) {
            this.gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private void setupControls() {
        InputMethod inputMethod = InputMethod.valueOf(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("input_method", "SWIPE"));

        //Only swipe and buttons support 3D
        if (is3d && inputMethod != InputMethod.SWIPE){
            inputMethod = InputMethod.BUTTONS;
        }

        LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.buttonControlsContainer);
        assert buttonsLayout != null;
        buttonsLayout.setVisibility(View.GONE);

        LinearLayout verticalButtonsLayout = (LinearLayout) findViewById(R.id.verticalButtonControlsContainer);
        verticalButtonsLayout.setVisibility(View.GONE);

        //Make sure we set up our buttons
        if (buttonController == null) {
            buttonController = new SnakeButtonController();
            addControllerButton(buttonsLayout, Direction.NORTH);
            addControllerButton(buttonsLayout, Direction.SOUTH);
            addControllerButton(buttonsLayout, Direction.WEST);
            addControllerButton(buttonsLayout, Direction.EAST);
            addControllerButton(verticalButtonsLayout, Direction.UP);
            addControllerButton(verticalButtonsLayout, Direction.DOWN);
        }

        //Make sure we set the vertical buttons to visible in 3d mode
        if (is3d) {
            verticalButtonsLayout.setVisibility(View.VISIBLE);
        }

        switch (inputMethod) {
            case SWIPE:
                //Initialize the swipe controller
                if (swipeController == null) {
                    swipeController = new SnakeSwipeController(game);
                    gestureListener.addGestureListener(swipeController);
                }
                snakeController = swipeController;
                break;
            case BUTTONS:
                //Initialize the button controller
                buttonsLayout.setVisibility(View.VISIBLE);
                snakeController = buttonController;
                break;
            case ACCELEROMETER:
                snakeController = accelerometerController;
                break;
            case COMPASS:
                snakeController = compassController;
                break;
            case GPS:
                snakeController = gpsController;
                break;
        }

        if (startFinishGestureListener == null) {
            startFinishGestureListener = new StartFinishGestureListener();
            gestureListener.addGestureListener(startFinishGestureListener);
        }
        startFinishGestureListener.setGame(game);

        if (is3d && inputMethod != InputMethod.BUTTONS) {
            snakeController = new SnakeAlmost3DController(snakeController, buttonController);
        }
        game.setSnakeController(snakeController);
    }

    /**
     * Adds a controller button for the specified direction to a pane
     *
     * @param to        The pane to add the button to
     * @param direction The direction for the button
     */
    private void addControllerButton(LinearLayout to, Direction direction) {
        Button button = new Button(this);

        int resourceId = android.R.style.TextAppearance_Small;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setTextAppearance(resourceId);
        } else {
            button.setTextAppearance(this, resourceId);
        }
        button.setText(direction.toString());

        button.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        to.addView(button);

        buttonController.bindButton(button, direction);
    }
}
