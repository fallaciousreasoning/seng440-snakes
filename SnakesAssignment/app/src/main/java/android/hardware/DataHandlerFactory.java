package android.hardware;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by comqdhb on 17/07/2016.
 */
public class DataHandlerFactory {


    private static HashMap<String, DataHandler> handlers = null;

    private static HashMap<String, DataHandler> initHandlers() {
        HashMap<String, DataHandler> result = new HashMap<>();
        result.put("list", new ListDataHandler());
        result.put("sensorEvent", new SensorEventHandler());
        return result;
    }


    public static void respondToData(String data, InjectableSensorManager ism) {
        try {
            JSONObject json = new JSONObject(data);
            String type = json.getString("TYPE");
            DataHandler handler = getHandlers().get(type);
            System.out.println("aaaaaaaaa " + type);
            if (handler != null) {
                System.out.println("bbbbbbbbb " + type);
                handler.handleData(json, ism);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, DataHandler> getHandlers() {
        if (handlers == null) {
            handlers = initHandlers();
        }
        return handlers;
    }

    public interface DataHandler {
        public void handleData(JSONObject json, InjectableSensorManager ism);
    }

    private static class SensorEventHandler implements DataHandler {

        @Override
        public void handleData(JSONObject json, InjectableSensorManager ism) {
            ism.raiseSensorEvent(new MySensorEvent(json, ism));
        }

    }

    public static class ListDataHandler implements DataHandler {

        @Override
        public void handleData(JSONObject json, InjectableSensorManager ism) {
            ism.sendList();
        }
    }


}
