package androidsensorinjector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helps build messages
 */
public class MessageBuilder {
    /**
     * The list of sensors we have to send messages to
     */
    private List<Integer> requiredSensors = new ArrayList<>();

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
        builders.put(InputMethod.Accelerometer, new AccelerometerValuesBuilder());
    }

    /**
     * Sets the input method
     * @param method The input method
     * @return The builder
     */
    public MessageBuilder setInputMethod(InputMethod method) {
        requiredSensors.clear();
        //Work out what sensors we need
        switch (method) {
            case Accelerometer:
                requiredSensors.add(InputMethod.Accelerometer.getType());
                break;
            case Compass:
                requiredSensors.add(InputMethod.Accelerometer.getType());
                requiredSensors.add(InputMethod.Compass.getType());
                break;
        }

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
     * Creates the sensor events required for this messages
     * @return The sensor events
     */
    public List<SensorEvent> createEvents() {
        SensorValueBuilder builder = builders.get(inputMethod);

        List<float[]> values = builder.getValues(direction);

        long timeStamp = this.timeStamp == -1 ? System.currentTimeMillis() : this.timeStamp;

        List<SensorEvent> events = new ArrayList<>();
        for (int i = 0; i < requiredSensors.size(); ++i) {
            Integer sensor = requiredSensors.get(i);
            SensorEvent event = new SensorEvent("sensorEvent", accuracy, sensor, timeStamp, values.get(i));
            events.add(event);
        }
        return events;
    }
}
