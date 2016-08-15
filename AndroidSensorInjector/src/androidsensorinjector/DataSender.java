/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author comqdhb
 */
public class DataSender {
    private final Socket socket;
    private ObjectOutputStream oos=null;
    private BlockingDeque<JsonSerializable> sendQueue = new LinkedBlockingDeque<>();

    private Thread senderThread;

    public DataSender(Socket s){
        socket=s;
        try {
            oos=new ObjectOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(DataSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendEvent(final JsonSerializable e){
        if (e==null){ return; }
        sendQueue.offer(e);

        if (senderThread == null) {
            Runnable r = () -> {
                while (true) {
                    try {
                        JsonSerializable event = sendQueue.pollFirst(50, TimeUnit.MILLISECONDS);
                        if (event == null) continue;
                        oos.writeObject(event.json());
                        oos.flush();
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            };

            senderThread = new Thread(r);
            senderThread.start();
        }
    }
}
