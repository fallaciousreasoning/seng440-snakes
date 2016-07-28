/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author comqdhb
 */
public class DataSender {
    private final Socket socket;
    private ObjectOutputStream oos=null;
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
        Runnable r =new Runnable(){

            @Override
            public void run() {
                try {
                    oos.writeObject(e.json());
                    oos.flush();
                } catch (IOException iOException) {
                }
            }
            
        };
        new Thread(r).start();
    }
}
