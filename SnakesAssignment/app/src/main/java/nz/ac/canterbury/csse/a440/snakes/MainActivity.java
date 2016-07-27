package nz.ac.canterbury.csse.a440.snakes;

//http://stackoverflow.com/questions/11184503/android-emulator-orientation-change-through-emulator-console-or-adb

import android.content.Context;
import android.graphics.Color;
import android.hardware.InjectableSensorManager;
import android.hardware.MySensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import nz.ac.canterbury.csse.a440.snakes.snake.CanvasViewRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.GameUpdater;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeSwipeController;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private long lastUpdate;
    private View view;
    private boolean color = false;
    private boolean debug=false;
    private TextView tv;

    private GestureDetectorCompat gestureDetector;
    private SnakeSwipeController swipeController;

    private SnakeController snakeController;

    SnakeGame game;
    GameUpdater updater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
;

        view = findViewById(R.id.tv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SnakeApplication app= (SnakeApplication) this.getApplication();
        InjectableSensorManager.setUseSystem(false);
        sensorManager = app.ism;
        if (sensorManager instanceof InjectableSensorManager){
            InjectableSensorManager ism= (InjectableSensorManager) sensorManager;
            //NB this needs to be a setting
            ism.createRemoteListener("132.181.102.62",51234);
        }

        for (Sensor s:sensorManager.getSensorList(Sensor.TYPE_ALL)){
            System.out.println(s.getName() + " "+s.getType());
        }

        Sensor acc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        CanvasViewRenderer gameRenderer = (CanvasViewRenderer) findViewById(R.id.gameRenderer);

        game = gameRenderer.getGame();
        updater = new GameUpdater(game);

        //Initialize the swipe controller
        swipeController = new SnakeSwipeController(game);
        gestureDetector = new GestureDetectorCompat(getBaseContext(), swipeController);

        //Set the snake controller
        snakeController = swipeController;
        game.setSnakeController(snakeController);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            tv.setText(event.timestamp+" "+event.toString());
            getAccelerometer(event);
        }

    }




    private void getAccelerometer(SensorEvent event) {
            float[] values = MySensorEvent.getValues(event);
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelationSquareRoot = (x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            long actualTime = event.timestamp;
        System.out.println("acc2 = "+accelationSquareRoot);
            if (accelationSquareRoot >= 2) //
            {
                Angles a=new Angles(values);
                if (actualTime - lastUpdate < 200) {
                    return;
                }
                lastUpdate = actualTime;
                Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT).show();
                if (color) {
                    view.setBackgroundColor(Color.GREEN);
                } else {
                    view.setBackgroundColor(Color.RED);
                }
                color = !color;
            }
    }

    //http://www.st.com/content/ccc/resource/technical/document/application_note/8e/28/c0/ea/1f/ed/4e/48/CD00268887.pdf/files/CD00268887.pdf/jcr:content/translations/en.CD00268887.pdf
    private class Angles{
        public final double pitch;
        public final double roll;

        public Angles(float[] values){
            float x=values[0];
            float y=values[1];
            float z=values[2];
            pitch=Math.atan(x/Math.sqrt(y*y+z*z));
            roll=Math.atan(y/Math.sqrt(x*x+z*z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
