package androidsensorinjector;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for generating the values we send to the compass
 */
public class CompassValuesBuilder implements SensorValueBuilder {
    @Override
    public List<float[]> getValues(Direction direction) {
        //Should return a list with two elements:
            //The value to send to the accelerometer
            //The value to send to the compass
        List<float[]> values = new ArrayList<>();

        float[] accelerometerValues = new float[3];
        float[] compassValues = new float[3];

        values.add(accelerometerValues);
        values.add(compassValues);

        switch (direction) {
            case NORTH:
                accelerometerValues[0] = -8.43f;
                accelerometerValues[1] = -15.16f;
                accelerometerValues[2] = 48.12f;

                compassValues[0] = 0.499f;
                compassValues[1] = 0.373f;
                compassValues[2] = 10.313f;
                break;
            case SOUTH:
                accelerometerValues[0] = 24.189f;
                accelerometerValues[1] = 18.808f;
                accelerometerValues[2] = 44.753f;

                compassValues[0] = 2.756f;
                compassValues[1] = 1.272f;
                compassValues[2] = 9.593f;
                break;
            case EAST:
                accelerometerValues[0] = 23.25f;
                accelerometerValues[1] = -16.72f;
                accelerometerValues[2] = 47.17f;

                compassValues[0] = 0.666f;
                compassValues[1] = 0.134f;
                compassValues[2] = 10.022f;
                break;
            case WEST:
                accelerometerValues[0] = -4.72f;
                accelerometerValues[1] = -0.124f;
                accelerometerValues[2] = 52.0f;

                compassValues[0] = 1.13f;
                compassValues[1] = 0.510f;
                compassValues[2] = 9.96f;
                break;
        }

        return values;
    }
}
