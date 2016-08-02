package androidsensorinjector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jayha on 28/07/2016.
 */
public class SensorListEvent implements JsonSerializable {
    private final String TYPE = "list";

    private List<SensorInfo> sensors;

    public List<SensorInfo> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorInfo> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String json() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"TYPE\": \"list\"");
        builder.append("}");
        return builder.toString();
    }
}
