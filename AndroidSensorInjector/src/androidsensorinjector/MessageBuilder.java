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

    private InputMethod inputMethod;
    private Direction direction;
    private int accuracy = 0;
    private long timeStamp = -1;

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

    public MessageBuilder setInputMethod(InputMethod method) {
        this.inputMethod = method;
        return this;
    }

    public MessageBuilder setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public MessageBuilder setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public MessageBuilder setTimeStamp(long timeStamp){
        this.timeStamp = timeStamp;
        return this;
    }

    public SensorEvent create() {
        SensorValueBuilder builder = builders.get(inputMethod);
        SensorInfo sensor = sensorInfo.get(inputMethod);

        float[] values = builder.getValues(direction);

        long timeStamp = this.timeStamp == -1 ? System.currentTimeMillis() : this.timeStamp;
        return new SensorEvent("sensorEvent", accuracy, sensor.getId(), timeStamp, values);
    }
}
