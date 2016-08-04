package nz.ac.canterbury.csse.a440.snakes;

//http://stackoverflow.com/questions/11184503/android-emulator-orientation-change-through-emulator-console-or-adb

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.InjectableSensorManager;
import android.hardware.MySensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dx.rop.code.Exceptions;

import nz.ac.canterbury.csse.a440.snakes.snake.CanvasViewRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.GameUpdater;
import nz.ac.canterbury.csse.a440.snakes.snake.InputMethod;
import nz.ac.canterbury.csse.a440.snakes.snake.ScoreRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeAccelerometerController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeButtonController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeCompassController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGLRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGLView;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGPSController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeSwipeController;
import nz.ac.canterbury.csse.a440.snakes.snake.StartFinishGestureListener;
import nz.ac.canterbury.csse.a440.snakes.snake.StartFinishRenderer;

public class MainActivity extends AppCompatActivity {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+1;
    private static final int LOCATION_COARSE_REQUEST=INITIAL_REQUEST+2;
    private static final int INTERNET_REQUEST=INITIAL_REQUEST+3;
    private SensorManager sensorManager;
    private LocationManager locationManager;

    private GestureDetectorCompat gestureDetector;
    private AggregateGestureListener gestureListener;

    private SnakeSwipeController swipeController;
    private SnakeAccelerometerController accelerometerController;
    private SnakeCompassController compassController;
    private SnakeButtonController buttonController;
    private SnakeGPSController gpsController;

    private SnakeController snakeController;
    private SnakeGLView gameGLRenderer;

    SnakeGame game;
    GameUpdater updater;

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
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }

        accelerometerController = new SnakeAccelerometerController();
        compassController = new SnakeCompassController();


        game = new SnakeGame(20, 30, 1, 3);

        CanvasViewRenderer gameRenderer = (CanvasViewRenderer) findViewById(R.id.gameRenderer);
        gameRenderer.setGame(game);

        gameGLRenderer = (SnakeGLView) findViewById(R.id.gameGLRenderer);
        game.addRenderer(gameGLRenderer);

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        ScoreRenderer scoreRenderer = new ScoreRenderer();
        scoreRenderer.setTextView(scoreText);
        game.addRenderer(scoreRenderer);

        TextView gameStatusText = (TextView) findViewById(R.id.gameStatusText);
        StartFinishRenderer startFinishRenderer = new StartFinishRenderer(getString(R.string.gameStatusStart), getString(R.string.gameStatusReset));
        startFinishRenderer.setTextView(gameStatusText);
        game.addRenderer(startFinishRenderer);

        updater = new GameUpdater(game);

        gestureListener = new AggregateGestureListener();
        gestureDetector = new GestureDetectorCompat(getBaseContext(), gestureListener);

        StartFinishGestureListener startFinishGestureListener = new StartFinishGestureListener(game);
        gestureListener.addGestureListener(startFinishGestureListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
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
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        SnakeApplication app = (SnakeApplication) this.getApplication();

        //Check if we should enable the sensor injector
        boolean sensor_injector_enabled = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("sensor_injector_enabled", false);
        InjectableSensorManager.setUseSystem(!sensor_injector_enabled);
        sensorManager = app.ism;
        if (sensorManager instanceof InjectableSensorManager) {
            InjectableSensorManager ism = (InjectableSensorManager) sensorManager;
            //Get the ip for the sensor injector
            String ip = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString("sensor_injector_ip", "127.0.0.1");
            ism.createRemoteListener(ip, 51234);
        }

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
        }
        catch (SecurityException e) {
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
        try {
            locationManager.removeUpdates(gpsController);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }

        gameGLRenderer.onPause();
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessLocationCoarse() {
        return(hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean canAccessInternet() {
        return(hasPermission(Manifest.permission.INTERNET));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.gestureDetector != null) {
            this.gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private void setupControls() {
        InputMethod inputMethod = InputMethod.valueOf(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("input_method", "SWIPE"));

        LinearLayout buttonsLayout = (LinearLayout)findViewById(R.id.buttonControlsContainer);
        buttonsLayout.setVisibility(View.GONE);

        switch (inputMethod){
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

                if (buttonController == null) {
                    buttonController = new SnakeButtonController();
                    addControllerButton(buttonsLayout, Direction.NORTH);
                    addControllerButton(buttonsLayout, Direction.SOUTH);
                    addControllerButton(buttonsLayout, Direction.WEST);
                    addControllerButton(buttonsLayout, Direction.EAST);
                }
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

        game.setSnakeController(snakeController);
    }

    /**
     * Adds a controller button for the specified direction to a pane
     * @param to The pane to add the button to
     * @param direction The direction for the button
     */
    private void addControllerButton(LinearLayout to, Direction direction){
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
