package nz.ac.canterbury.csse.a440.snakes.snake.controllers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;

/**
 * A controller for the snake that makes use of accelerometer data
 */
public class SnakeAccelerometerController implements SnakeController, SensorEventListener {
    private Direction direction = Direction.NORTH;
    private double initRoll;
    private double initPitch;

    @Override
    public Direction getDirection() {
        return direction;
    }


    @Override
    public void reset(Direction direction) {
        this.direction = direction;
        initPitch = 0;
        initRoll = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        double pitch = Math.atan(x / Math.sqrt(y * y + z * z));
        double roll = Math.atan(y / Math.sqrt(x * x + z * z));

        long actualTime = event.timestamp;
        if ((initPitch == 0 && initRoll == 0)) {
            initPitch = pitch;
            initRoll = roll;
            return;
        }
        if (Math.abs(pitch) > Math.abs(roll)) {
            if (pitch > 0) {
                direction = Direction.WEST;
            } else {
                direction = Direction.EAST;
            }
        } else {
            if (roll > 0) {
                direction = Direction.SOUTH;
            } else {
                direction = Direction.NORTH;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
