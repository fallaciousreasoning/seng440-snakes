/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author comqdhb
 */
public class AndroidSensorInjector extends Thread {

    public boolean isConnected() {
        return state == State.Accepted;
    }

    public enum State {

        NotStarted, Listening, Started, Accepted, Dead
    }

    private boolean ok = true;

    private static AndroidSensorInjector instance = null;

    public synchronized static AndroidSensorInjector getInjector() {
        if (instance == null) {
            try {
                instance = new AndroidSensorInjector();
            } catch (IOException ex) {
                Logger.getLogger(AndroidSensorInjector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    private AndroidSensorInjector() throws IOException {
        q = new ConcurrentLinkedQueue();
        listeners = new HashSet<>();
        start();
    }

    private Queue<SensorEvent> q;

    private Set<RemoteEventListener> listeners;

    public void addDataArrivalListener(RemoteEventListener dal) {
        listeners.add(dal);
    }

    public void removeDataArrivalListener(RemoteEventListener dal) {
        listeners.remove(dal);
    }

    public void addEvent(SensorEvent e) {
        if (isConnected()) {
            q.add(e);
        }
    }

    private State state = State.NotStarted;

    public State getRunningState() {
        return state;
    }

    public void run() {
        state = State.Started;
        ServerSocket ss;
        try {
            ss = new ServerSocket(51234);
            state = State.Listening;
            Socket s;
            while ((s = ss.accept()) != null) {
                state = State.Accepted;
                new ReceiverThread(s, new RemoteEventListener() {

                    @Override
                    public void receiveData(String data) {

                        listeners.stream().forEach((dal) -> {
                            dal.receiveData(data);
                        });
                    }

                    @Override
                    public void ended() {
                        ok = false;
                    }
                });

                DataSender ds = new DataSender(s);

                try {
                    //TODO your code to send the json list request
                    float[] data = new float[0];
                    ds.sendEvent(new SensorListEvent());
                    //

                    //Now for the main event dispatch thread....
                    while (ok) {
                        while (!q.isEmpty()) {
                            ds.sendEvent(q.remove());
                        }
                        Thread.sleep(1200);
                    }

                } catch (Exception iOE) {
                }
                state = State.Listening;
            }

        } catch (Exception e) {
        }
        state = State.Dead;
    }
}
