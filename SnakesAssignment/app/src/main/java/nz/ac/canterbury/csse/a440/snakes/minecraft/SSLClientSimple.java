/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.canterbury.csse.a440.snakes.minecraft;

import android.content.Context;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author pi
 */
public class SSLClientSimple extends SSLClient {
    

    
    public SSLClientSimple(Context applicationContext,char[] pwd){
        super(applicationContext,pwd);
    }

    @Override
    protected  void setupSSLKeysAndTrusts(Context applicationContext,char[] pwd) {
        System.setProperty("javax.net.ssl.keyStore", "clientkeystore.jks");
        System.setProperty("javax.net.ssl.trustStore", "clienttrusts.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        socketFactory = SSLSocketFactory.getDefault();
    }

}
