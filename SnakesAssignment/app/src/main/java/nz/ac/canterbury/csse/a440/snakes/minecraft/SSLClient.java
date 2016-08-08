/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.canterbury.csse.a440.snakes.minecraft;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author comqdhb
 */
public abstract class SSLClient {
    
    protected SocketFactory socketFactory;
    
    protected static final String host="127.0.0.1";
    protected static final int port=25555;
    protected static final int i=1;
    
    public SSLClient(Context applicationContext,char[] pwd){
        setupSSLKeysAndTrusts(applicationContext,pwd);
    }
    
    public void doit(Socket socket) throws IOException {
        socket.getOutputStream().write("hello\r\n".getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = br.readLine();
        System.out.println("<" + line);
    }
    
    public  Socket getSocket(String host,int port) throws Exception{
        SSLSocket sslSocket =null;
        sslSocket=(SSLSocket) socketFactory.createSocket(host, port);

        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession socketSession = sslSocket.getSession();
//if (!hv.verify(host, s)){err}
        String name = socketSession.getPeerPrincipal().getName();
        String cn = name.split(",")[0].split("=")[1];
        System.out.println("server cn="+cn);
        if (!host.equals(cn) && !"127.0.0.1".equals(host)) {
            throw new SSLHandshakeException("Expected " + host + ", "
                    + "found " + cn);
        }

        if (!host.equals(socketSession.getPeerHost())) {
            throw new SSLHandshakeException("Expected " + host + ", "
                    + "found " + cn);
        }
        return sslSocket;
    }

    protected abstract  void setupSSLKeysAndTrusts(Context applicationContext,char[] pwd);
}
