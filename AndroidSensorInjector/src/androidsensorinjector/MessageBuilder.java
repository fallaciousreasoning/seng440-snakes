package androidsensorinjector;

import jdk.internal.util.xml.impl.Input;
import sun.management.snmp.jvminstr.NotificationTarget;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Helps build messages
 */
public class MessageBuilder {
    /**
     * A hashmap which maps input methods to sensors
     */
    private static HashMap<InputMethod, SensorInfo> sensorInfo = new HashMap<>();

    /**
     * Maps an input method to a sensor
     * @param sensor The sensor
     */
    public static void registerInputMethod(InputMethod method, SensorInfo sensor) {
        sensorInfo.put(method, sensor);
    }

    /**
     * A map of builders for different sensors
     */
    private HashMap<InputMethod, SensorValueBuilder> builders = new HashMap<>();

    /**
     * The input method to build this command for
     */
    private InputMethod inputMethod;

    /**
     * The input direction to send
     */
    private Direction direction;

    /**
     * The accuracy of the injected event
     */
    private int accuracy = 0;

    /**
     * The timestamp for the injected event. Defaults to no
     */
    private long timeStamp = -1;

    /**
     * Creates a new message builder
     */
    public MessageBuilder() {
        builders.put(InputMethod.Accelerometer, dir -> {
            float[] values = new float[3];
            float magicNumber = 10;

            values[2] = magicNumber;

            switch (dir) {
                case NORTH:
                    values[1] = -magicNumber;
                    values[0] = 0;
                    break;
                case SOUTH:
                    values[1] = magicNumber;
                    values[0] = 0;
                    break;
                case EAST:
                    values[0] = -magicNumber;
                    values[1] = 0;
                    break;
                case WEST:
                    values[0] = magicNumber;
                    values[1] = 0;
                    break;
            }

            return values;
        });
    }

    /**
     * Sets the input method
     * @param method The input method
     * @return The builder
     */
    public MessageBuilder setInputMethod(InputMethod method) {
        this.inputMethod = method;
        return this;
    }

    /**
     * Sets the input direction
     * @param direction The direction
     * @return The builder
     */
    public MessageBuilder setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    /**
     * Sets the accuracy
     * @param accuracy The accuracy
     * @return The builder
     */
    public MessageBuilder setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    /**
     * Sets the timestamp
     * @param timeStamp The timestamp
     * @return The builder
     */
    public MessageBuilder setTimeStamp(long timeStamp){
        this.timeStamp = timeStamp;
        return this;
    }

    /**
     * Creates the sensor event
     * @return The sensor event
     */
    public SensorEvent create() {
        SensorValueBuilder builder = builders.get(inputMethod);
        SensorInfo sensor = sensorInfo.get(inputMethod);

        float[] values = builder.getValues(direction);

        long timeStamp = this.timeStamp == -1 ? System.currentTimeMillis() : this.timeStamp;
        return new SensorEvent("sensorEvent", accuracy, sensor.getId(), timeStamp, values);
    }
}
