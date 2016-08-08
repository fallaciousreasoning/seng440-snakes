/*

 #GENERATE SERVER keys and certificate
 keytool -genkey -keyalg RSA -alias serverselfsigned -keystore serverkeystore.jks -storepass password -validity 360 -keysize 2048

 #EXPORT Server Certificate 
 keytool -export -alias serverselfsigned -keystore serverkeystore.jks -rfc -file server.cer

 #ADD the server certificate to the clientsTrust keystore
 keytool -import -alias serverselfsigned -file server.cer -keystore clienttrusts.jks



 #GENERATE Client keys and certificate
 keytool -genkey -keyalg RSA -alias clientselfsigned -keystore clientkeystore.jks -storepass password -validity 360 -keysize 2048

 #EXPORT Client Certificate 
 keytool -export -alias clientselfsigned -keystore clientkeystore.jks -rfc -file client.cer

 #ADD the client certificate to the serverTrusts keystore
 keytool -import -alias clientselfsigned -file client.cer -keystore servertrusts.jks

 rm *.cer

 */
package nz.ac.canterbury.csse.a440.snakes.minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author pi
 */
public class SSLServerSocketDemo {

    /**
     * @param args the command line arguments
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // TODO code application logic here

        SSLServerSocket sslServerSocket=creatSSLServerSocket(25555);
        
        Socket ssock;

        while ((ssock = sslServerSocket.accept()) != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(ssock.getInputStream()));
                String line = br.readLine();
                line += "\r\n";
                System.out.print(">" + line);
                ssock.getOutputStream().write(line.getBytes());
                ssock.getOutputStream().flush();
                ssock.close();
            } catch (IOException iOException) {
            }
        }
    }

    private static SSLServerSocketFactory sslsf = null;

    public static SSLServerSocket creatSSLServerSocket(int port) throws IOException {
        if (sslsf == null) {
            setupSSL();
        }
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslsf.createServerSocket(port);

        sslServerSocket.setNeedClientAuth(true);
        return sslServerSocket;
    }

    private static void setupSSL() {
        System.setProperty("javax.net.ssl.keyStore", "serverkeystore.jks");
        System.setProperty("javax.net.ssl.trustStore", "servertrusts.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        sslsf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

    }

}
