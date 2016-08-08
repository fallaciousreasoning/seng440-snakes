package androidsensorinjector;

import java.util.List;

/**
 * An enum describing different input methods
 */
public enum InputMethod {
    Accelerometer("android.sensor.accelerometer"),
    Compass("android.sensor.magnetic_field");

    private InputMethod(String name) {
        this.name = name;
    }

    /**
     * Initialize the types for our sensor list
     * @param sensorInfoList The list of device sensors
     */
    public static void initTypes(List<SensorInfo> sensorInfoList) {
        for (SensorInfo info : sensorInfoList) {
            for (InputMethod method : values()) {
                if (method.name.equals(info.getName()))
                    method.type = info.getType();
            }
        }
    }

    /**
     * The name of the associated sensor
     */
    private String name;

    private int type;

    /**
     * Gets the type for the sensor
     * @return The type
     */
    public int getType(){
        return type;
    }


}
