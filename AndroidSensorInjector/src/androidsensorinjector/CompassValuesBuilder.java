package androidsensorinjector;

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
        return null;
    }
}
