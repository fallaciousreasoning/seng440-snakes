package androidsensorinjector;

/**
 * A class for holding information about sensors
 */
public class SensorInfo {
    private int id;
    private String name;

    /**
     * Gets the id of the sensor
     * @return The id of the sensor
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the sensor
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
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
