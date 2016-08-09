package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClient;
import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClientWithResources;

/**
 * Created by wooll on 09-Aug-16.
 */
public class MinecraftControls {

    private Context context;
    private static final String defaultUrl = "csse-minecraft2.canterbury.ac.nz";
    private static final int defaultPort = 25555;
    private static final String defaultToken = "03e699db46ee679c79277f11368aa289";
    private String url;
    private int port;
    private BufferedReader reader;
    private PrintWriter writer;

    public MinecraftControls(Context context) {
        this(context, defaultUrl, defaultPort);
    }

    public MinecraftControls(Context context, String url, int port) {
        this.context = context;
        this.url = url;
        this.port = port;
    }

    public void ConnectToSocket() {
        try {
            SSLClient sslClient = new SSLClientWithResources(context, "".toCharArray());
            Socket socket = sslClient.getSocket(url, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
