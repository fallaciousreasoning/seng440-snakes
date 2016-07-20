/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author comqdhb
 */
public class DemoDataSource extends Thread {

    private final Random rnd;
    private final boolean ok;
    private final float G2 = 9.81f * 2f;

    public static void main(String[] args) {
        new DemoDataSource();
    }

    public DemoDataSource() {
        rnd = new Random();
        ok = true;
        start();
    }

    public void run() {
        AndroidSensorInjector asi = AndroidSensorInjector.getInjector();
        SensorEvent se;
        while (ok) {
            if (asi.isConnected()) {
                se = new SensorEvent("sensorEvent", 0, 1, System.currentTimeMillis(), new float[]{rnd.nextFloat() * G2, rnd.nextFloat() * G2, rnd.nextFloat() * G2});
                asi.addEvent(se);
                System.out.println(se.json());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DemoDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
