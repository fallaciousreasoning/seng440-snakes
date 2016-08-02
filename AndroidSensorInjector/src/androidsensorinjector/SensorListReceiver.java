package androidsensorinjector;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jayha on 28/07/2016.
 */
public class SensorListReceiver implements RemoteEventListener {
    private SensorListEvent availableSensors;

    @Override
    public void receiveData(String data) {
        JSONObject list = new JSONObject(data);

        List<SensorInfo> sensorList = new ArrayList<>();

        //I admit, this is not how I should be doing things. However, org.json is a pain
        String[] sensors = list.getString("sensors").split(",");
        String[] types = list.getString("types").split(",");

        for (int i = 0; i < sensors.length; ++i) {
            SensorInfo info = new SensorInfo();
            info.setName(sensors[i]);
            info.setType(Integer.parseInt(types[i]));
            sensorList.add(info);
        }

        availableSensors = new SensorListEvent();
        availableSensors.setSensors(sensorList);
        InputMethod.initTypes(sensorList);
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
