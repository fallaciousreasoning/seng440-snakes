package androidsensorinjector;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jayha on 28/07/2016.
 */
public class SensorListReceiver implements RemoteEventListener {
    private SensorListEvent availableSensors;

    @Override
    public void receiveData(String data) {
        JSONObject list = new JSONObject(data);
        Set<String> sensorList = new HashSet<>();

        for (Object sensor : list.getJSONArray("sensors")) {
            if (!(sensor instanceof String)) {
                throw new RuntimeException("What on earth are you doing?");
            }

            sensorList.add(sensor.toString());
        }

        availableSensors = new SensorListEvent();
        availableSensors.setSensors(sensorList);
    }

    /**
     * Gets a list of the sensors this listener has been told are available
     * @return the sensors
     */
    public SensorListEvent getAvailableSensors() {
        return availableSensors;
    }

    @Override
    public void ended() {

    }
}