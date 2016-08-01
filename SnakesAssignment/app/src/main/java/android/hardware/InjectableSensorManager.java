package android.hardware;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * Created by comqdhb on 17/07/2016.
 */
public class InjectableSensorManager extends SensorManager {

private static boolean useSystem=true;

    private final SensorManager sm;

    private final Map<SensorListener,Set<Integer>> listeners;
    private final Map<SensorEventListener,Set<Sensor>> listenersE;
    private final Context context;
    private final Handler handler;
    private  RemoteListener remoteListener;


    private static InjectableSensorManager instance=null;

    public static InjectableSensorManager getInstance(Application a) {
        if (instance==null){
            instance=new InjectableSensorManager(a);
        }
        return instance;
    }
    /**
     * {@hide}
     */
    private InjectableSensorManager(Context c) {
        context=c;
        handler=new Handler();
        sm=(SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        listeners=new HashMap<>();
        listenersE=new HashMap<>();
        triggers=new HashMap<>();
    }

    public static boolean isUseSystem() {
        return useSystem;
    }

    public static void setUseSystem(boolean useSystem) {
        InjectableSensorManager.useSystem = useSystem;
    }

    public boolean createRemoteListener(String host,int port){
        boolean done=false;
        try {
            remoteListener=new RemoteListener(host,port,this);
            done=true;
            System.out.println("New RemoteListener created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    @Override
    public int getSensors() {

        return sm.getSensors();
    }

    @Override
    public List<Sensor> getSensorList(int type) {
        return sm.getSensorList(type);
    }

    @Override
    public Sensor getDefaultSensor(int type) {
        return sm.getDefaultSensor(type);
    }

    @Override
    public Sensor getDefaultSensor(int type, boolean wakeUp) {
        return sm.getDefaultSensor(type, wakeUp);
    }






    ////////////////




    @Override
    public boolean registerListener(SensorListener listener, int sensors) {
        if (!isUseSystem()) {
            return regList(listener, sensors);
        }

        return sm.registerListener(listener, sensors);

    }

    private boolean regList(SensorListener listener, int sensors){

        Set<Integer> s=listeners.get(listener);
        if (s==null){
            s=new HashSet<>();
        }
        s.add(sensors);
        return true;
    }

    @Override
    public boolean registerListener(SensorListener listener, int sensors, int rate) {
        if (!isUseSystem()) { return regList(listener,sensors);}
        return sm.registerListener(listener, sensors, rate);
    }

    @Override
    public void unregisterListener(SensorListener listener) {
        sm.unregisterListener(listener);
        listeners.remove(listener);
    }

    @Override
    public void unregisterListener(SensorListener listener, int sensors) {
        sm.unregisterListener(listener, sensors);
        removeSensor(listener,sensors);
    }

    private boolean removeSensor(SensorListener listener, int sensors){
        Set<Integer> s=listeners.get(listener);
        if (s!=null){
            s.remove(sensors);
        }
        return true;
    }

    @Override
    public void unregisterListener(SensorEventListener listener, Sensor sensor) {
        if (isUseSystem()) {
            sm.unregisterListener(listener, sensor);
        }
            removeSensorE(listener, sensor.getType());

    }

    private void removeSensorE(SensorEventListener listener, int sensors){
        Set<Sensor> s=listenersE.get(listener);
        if (s!=null){
            s.remove(sensors);
        }
    }

    @Override
    public void unregisterListener(SensorEventListener listener) {
        sm.unregisterListener(listener);
        listenersE.remove(listener);
    }

    private boolean regListE(SensorEventListener listener, Sensor sensor){
        Set<Sensor> s=listenersE.get(listener);
        if (s==null){
            s=new HashSet<>();
        }
        s.add(sensor);
        listenersE.put(listener,s);
        return true;
    }

    @Override
    public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs) {
        if (!isUseSystem()) { return regListE(listener,sensor);}
        return sm.registerListener(listener, sensor, samplingPeriodUs);
    }

    @Override
    public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs) {
        if (!isUseSystem()) { return regListE(listener, sensor);}
        return sm.registerListener(listener, sensor, samplingPeriodUs, maxReportLatencyUs);
    }

    @Override
    public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs, Handler handler) {
        if (!isUseSystem()) { return regListE(listener,sensor);}
        return sm.registerListener(listener, sensor, samplingPeriodUs, handler);
    }

    @Override
    public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs, Handler handler) {
        if (!isUseSystem()) { return regListE(listener, sensor);}
        return sm.registerListener(listener, sensor, samplingPeriodUs, maxReportLatencyUs, handler);
    }

    @Override
    public boolean flush(SensorEventListener listener) {
        return sm.flush(listener);
    }


    private final Map<TriggerEventListener,Set<Sensor>> triggers;

    @Override
    public boolean requestTriggerSensor(TriggerEventListener listener, Sensor sensor) {
        Set<Sensor> s= triggers.get(listener);
        if (s==null){
            s=new HashSet<>();
        }
        s.add(sensor);
        triggers.put(listener,s);
        return sm.requestTriggerSensor(listener, sensor);
    }

    @Override
    public boolean cancelTriggerSensor(TriggerEventListener listener, Sensor sensor) {
        Set<Sensor> s= triggers.get(listener);
        if (s==null){
            s=new HashSet<>();
        }
        s.remove(sensor);
        triggers.put(listener,s);
        return sm.cancelTriggerSensor(listener, sensor);
    }








    public  void raiseSensorEvent(final MySensorEvent lr) {
        Runnable r=new Runnable(){
          public void run(){
              for (SensorEventListener l:listenersE.keySet()){
                  Set<Sensor> s=listenersE.get(l);
                  if (s.contains(lr.sensor) ){
                      Log.d("SENSOR_DATA", lr.toString());
                      l.onSensorChanged(lr);
                  }
              }
          }
        };
        handler.post(r);
    }










    public Sensor getSensor(int i) {
        for (Sensor s:sm.getSensorList(Sensor.TYPE_ALL)){
            if (s.getType()==i){
                return s;
            }
        }
        return null;
    }

    public void sendList() {
        try {
            JSONObject json = new JSONObject();
            json.put("TYPE", "list");

            //We do it this way because JSON.org is hopeless and won't serialize my lists like I want
            StringBuilder sensors = new StringBuilder();

            for (Sensor s : getSensorList(Sensor.TYPE_ALL)) {
                if (sensors.length() > 0) {
                    sensors.append(",");
                }
                sensors.append(s.getName());
            }

            json.put("sensors", sensors.toString());

            //TODO make the json actually have a list of sensors on the device
            remoteListener.sendString(json.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class RemoteListener extends Thread {
        private final String host;
        private final InjectableSensorManager ism;
        private final int port;
        private EventReceiver er = null;
        public  EventSender es=null;

        RemoteListener (String host,int port,InjectableSensorManager ism)  {
            this.host=host;
            this.ism=ism;
            this.port=port;
            this.start();}

        public void run(){
            Socket s;
            try {
                s= new Socket(host,port);
                er = new EventReceiver(s.getInputStream(),ism);
                es= new EventSender(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void sendString(String s) {
            if (es == null) return;
            es.sendData(s);
        }

        private class EventReceiver extends Thread {
            private final ObjectInputStream ois;
            private  InjectableSensorManager ism;

            public EventReceiver(InputStream inputStream, InjectableSensorManager ism) throws IOException {
                this.ism = ism;
                ois = new ObjectInputStream(inputStream);
                this.start();
            }
            public void run() {
                String data;
                try {
                    while ( (data=(String) ois.readObject())!=null){
                        DataHandlerFactory.respondToData(data,ism);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public class EventSender {
            private ObjectOutputStream ous=null;

            public EventSender(OutputStream outputStream) throws IOException, ClassNotFoundException {
                try {
                    ous= new ObjectOutputStream(outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void sendData(final String data){
                Runnable r=new Runnable(){
                    @Override
                    public void run() {
                        try {
                            ous.writeObject(data);
                            ous.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(r).start();
            }
        }
    }
}
