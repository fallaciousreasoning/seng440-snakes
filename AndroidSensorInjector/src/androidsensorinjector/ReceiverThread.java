/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author comqdhb
 */
class ReceiverThread extends Thread {
    private final Socket socket;
    private final RemoteEventListener listener;

    public ReceiverThread(Socket s,RemoteEventListener l) {
        socket=s;
        listener=l;
        start();
    }

    
    
    @Override
    public void run(){
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object o;
            while ((o = ois.readObject()) != null) {
                if (listener != null) {
                    listener.receiveData((String) o);
                }
            }
        } catch (Exception iOE) {
            //iOE.printStackTrace();
        } 
        listener.ended();
    }
    
}
