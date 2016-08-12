package android.hardware;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by comqdhb on 17/07/2016.
 */
public class MySensorEvent extends SensorEvent {


    private float[] v = null;
    private String json = "json";

    public MySensorEvent(JSONObject data, InjectableSensorManager ism) {
        try {
            json = data.toString();
            accuracy = data.getInt("accuracy");
            sensor = ism.getSensor(data.getInt("sensor"));
            timestamp = data.getLong("timestamp");
            JSONArray fa = data.getJSONArray("values");
            int length = fa.length();
            v = new float[length];
            for (int i = 0; i < length; i++) {
                v[i] = (float) fa.getDouble(i);
            }

            Class c = this.getClass();
            //now to use reflection to try and set the final variable values
            try {
                Field df = c.getField("values");
                setFinal(df, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("values[ 0 ]="+values[0]);
    }

    public static float[] getValues(SensorEvent e) {
        if (e instanceof MySensorEvent) {
            MySensorEvent mse = (MySensorEvent) e;
            return mse.getValues();
        }
        return e.values;
    }

    public int getType() {
        if (sensor == null) {
            return Sensor.TYPE_ACCELEROMETER;
        }
        return sensor.getType();
    }

    void setFinal(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field[] fas = Field.class.getFields();
        for (Field f : fas) {
            System.out.println("  " + f.getName());
        }
        fas = Field.class.getDeclaredFields();
        for (Field f : fas) {
            System.out.println("D " + f.getName());
        }

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        field.set(this, newValue);
    }

    public String toString() {
        return json;
    }

    public float[] getValues() {
        return v;
    }
}
