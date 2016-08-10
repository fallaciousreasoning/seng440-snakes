package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClient;
import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClientWithResources;

/**
 * Created by wooll on 09-Aug-16.
 */
public class MinecraftControls implements Runnable {

    public enum BLOCKTYPE {
        CLEAR(0),
        HEAD(4),
        BODY(2),
        FOOD(3);

        private final int value;

        BLOCKTYPE(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    private Context context;
    private static final String defaultUrl = "csse-minecraft2.canterbury.ac.nz";
    private static final int defaultPort = 25555;
    private static final String defaultToken = "03e699db46ee679c79277f11368aa289";
    private String url;
    private int port;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private static final int XMAX = 100;
    private static final int XMIN = 0;
    private static final int YMIN = 0;
    private static final int YMAX = 100;
    private static final int ZMIN = 0;
    private static final int ZMAX = 100;

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
            SSLClient sslClient = new SSLClientWithResources(context, "password".toCharArray());
            socket = sslClient.getSocket(url, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writeToSocket(defaultToken);

            clearArea();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBlock(int x, int y, int z, BLOCKTYPE type) throws IOException {
        writeToSocket("world.setBlock(" + x + "," + y + "," + z + "," + type.getValue() +")");
    }

    public int readBlock(int x, int y, int z) throws IOException {
        writeToSocket("world.getBlock(" + x + "," + y + "," + z + ")");
        String result = readToSocket();
        //TODO parse result
        Log.w("Minecrat", result);
        return 0;
    }

    private String readToSocket() throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            output.append(line);
        }
        return output.toString();
    }

    private void clearArea() {
        try {
            for (int x = XMIN; x < XMAX; x++) {
                for (int y = YMIN; y < YMAX; y++) {
                    for (int z = ZMIN; z < ZMAX; z++) {
                        writeBlock(x, y, z, BLOCKTYPE.CLEAR);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToSocket(String message) throws IOException {
        char[] chars = (message + "\r\n").toCharArray();
        writer.write(chars);
        writer.flush();
    }

    public void close() {
        try {
            writer.close();
            reader.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ConnectToSocket();
    }
}
