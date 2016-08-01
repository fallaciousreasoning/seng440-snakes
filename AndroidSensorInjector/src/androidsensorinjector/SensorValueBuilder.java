package androidsensorinjector;

/**
 * An interface for building the values for a sensor
 */
public interface SensorValueBuilder {
    float[] getValues(Direction direction);
}
