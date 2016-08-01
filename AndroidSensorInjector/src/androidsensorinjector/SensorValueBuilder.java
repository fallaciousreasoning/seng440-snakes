package androidsensorinjector;

import java.util.List;

/**
 * An interface for building the values for a sensor
 */
public interface SensorValueBuilder {
    /**
     * Gets a list, each element of which corresponds to the values for a different sensor
     * @param direction The direction
     * @return The values for each sensor
     */
    List<float[]> getValues(Direction direction);
}
