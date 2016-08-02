package androidsensorinjector;

/**
 * A class for holding information about sensors
 */
public class SensorInfo {
    private int type;
    private String name;

    /**
     * Gets the type of the sensor
     * @return The type of the sensor
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of the sensor
     * @param type The type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the name of the sensor
     * @return The name of the sensor
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sensor
     * @param name The name of the sensor
     */
    public void setName(String name) {
        this.name = name;
    }
}
