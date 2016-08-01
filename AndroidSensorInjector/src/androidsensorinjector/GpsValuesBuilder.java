package androidsensorinjector;

import java.util.List;

/**
 * A class for building the values to send to our GPS
 */
public class GpsValuesBuilder implements SensorValueBuilder {
    /**
     * The last latitude sent
     */
    public static float lastLatitude;

    /**
     * The last longitude sent
     */
    public static float lastLongitude;

    @Override
    public List<float[]> getValues(Direction direction) {
        //Should return a list with one value
            //The lat/lng for the gps
        return null;
    }
}
