/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.canterbury.csse.a440.snakes.minecraft;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import nz.ac.canterbury.csse.a440.snakes.R;

/**
 * @author pi
 */
public class SSLClient {

    private static final String host = "csse-minecraft2.canterbury.ac.nz";
    private static final int port = 25555;
    private SSLSocketFactory socketFactory;

    public SSLClient(Context applicationContext, char[] pwd) {
        setupSSLKeysAndTrusts(applicationContext, pwd);
    }

    protected void setupSSLKeysAndTrusts(Context applicationContext, char[] pwd) {
        try {


            // Create a memory KeyStore containing our trusted CAs
            //String keyStoreType = KeyStore.getDefaultType();

            Resources resources = applicationContext.getResources();

            KeyStore systemKeyStore = KeyStore.getInstance("AndroidKeyStore");

            systemKeyStore.load(null);

            KeyStore keyStore = KeyStore.getInstance("BKS");
            for (int i : new int[] {R.raw.clientkeystore, R.raw.clienttrusts}) {
                keyStore.load(resources.openRawResource(i), pwd);//nb creates a new one from the resource

                Enumeration<String> la = keyStore.aliases();
                while (la.hasMoreElements()) {
                    String alias = la.nextElement();
                    System.out.println(alias);


                    if (keyStore.isKeyEntry(alias)) {
                        PrivateKey key = (PrivateKey) keyStore.getKey(alias, pwd);
                        Certificate[] certs = keyStore.getCertificateChain(alias);
                        systemKeyStore.setKeyEntry(alias, key, null, certs);
                    }
                    if (keyStore.isCertificateEntry(alias)) {
                        Certificate cert = keyStore.getCertificate(alias);
                        systemKeyStore.setCertificateEntry(alias, cert);
                    }


                }
            }


            // Create a TrustManager that trusts the CAs in our systemKeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(systemKeyStore);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            kmf.init(systemKeyStore, null);

            // Create an SSLContext that uses our kmf and TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            socketFactory = context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(1);// may be a bit harsh
        }
    }

    public SSLSocket getSocket() {
        try {
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(host, port);
            sslSocket.setTcpNoDelay(true);
            sslSocket.setKeepAlive(true);
            sslSocket.setTrafficClass(0x10);
            return sslSocket;
        } catch (IOException e) {
            throw new RuntimeException("Well that failed");
        }
    }
}
