package nz.ac.canterbury.csse.a440.snakes.snake.controllers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;

/**
 * Created by wooll on 29-Jul-16.
 */
public class SnakeCompassController implements SnakeController, SensorEventListener {
    private Direction direction = Direction.NORTH;
    private float[] acceleration;
    private float[] magnetic;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAcceleration(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            getMagneticField(event);
        }
    }

    private void getMagneticField(SensorEvent event) {
        acceleration = event.values;
        getOrientation();
    }

    private void getAcceleration(SensorEvent event) {
        magnetic = event.values;
        getOrientation();
    }

    private void getOrientation() {
        if (acceleration != null && magnetic != null) {
            SensorManager.getRotationMatrix(mR, null, acceleration, magnetic);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 405) % 360;
            int quartile = (int) azimuthInDegrees / 90;
            switch (quartile) {
                case 0:
                    direction = Direction.NORTH;
                    break;
                case 1:
                    direction = Direction.EAST;
                    break;
                case 2:
                    direction = Direction.SOUTH;
                    break;
                case 3:
                    direction = Direction.WEST;
                    break;
                default:
                    direction = Direction.NORTH;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void reset(Direction direction) {
        this.direction = direction;
        acceleration = null;
        magnetic = null;
    }
}
