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

        String[] sensors = list.getString("sensors").split(",");
        for (String sensor : sensors) {
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
