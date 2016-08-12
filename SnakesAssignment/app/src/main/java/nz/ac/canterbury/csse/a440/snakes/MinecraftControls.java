package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClientWithResources;
import nz.ac.canterbury.csse.a440.snakes.snake.MinecraftSubscriber;

/**
 * Created by wooll on 09-Aug-16.
 */
public class MinecraftControls implements Runnable {

    private static final String defaultUrl = "csse-minecraft2.canterbury.ac.nz";
    private static final int defaultPort = 25555;
    private static final String defaultToken = "03e699db46ee679c79277f11368aa289";
    public static final int XMAX = 100;
    public static final int XMIN = 0;
    public static final int YMIN = 0;
    public static final int YMAX = 100;
    public static final int ZMIN = 0;
    public static final int ZMAX = 100;
    private final SSLClientWithResources sslClient;
    private String url;
    private int port;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MinecraftSubscriber> subscribers = new ConcurrentLinkedQueue<>();
    private boolean stop = false;
    private boolean connected = false;
    private Messaging messaging;

    public MinecraftControls(Context context) {
        this(context, defaultUrl, defaultPort);
    }

    public MinecraftControls(Context context, String url, int port) {
        this.url = url;
        this.port = port;
        sslClient = new SSLClientWithResources(context, "password".toCharArray());
    }

    private void connectToSocket() {
        try {
            socket = sslClient.getSocket(url, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writeToSocket(defaultToken);
//            readFromSocket();
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Messaging getMessaging() {
        if (messaging == null) {
            messaging = new Messaging();
        }
        return  messaging;
    }

    public String readFromSocket() throws IOException {
        if (reader != null) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        }
        return "";
    }


    private void writeToSocket(String message) throws IOException {
        char[] chars = (message + "\r\n").toCharArray();
        writer.write(chars);
        writer.flush();
    }

    public void close() {
        stop = true;
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Messaging implements Runnable {

        boolean finished = false;

        @Override
        public void run() {
            while (!connected) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                while (!finished) {
                    if (!messageQueue.isEmpty()) {
                        writeToSocket(messageQueue.poll());
                    }
                    String result = readFromSocket();
                    if (result.length() > 0) {
                        results.add(result);
                        notifyRecievers();
                    }
                }
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeBlocks(int x, int y, int z, int x2, int y2, int z2, BLOCKTYPE type) throws IOException {
            messageQueue.add("world.setBlocks(" + x + "," + y + "," + z + "," + x2 + "," + y2 + "," + z2 + "," + type.getValue() + ")");
        }

        public void writeBlock(int x, int y, int z, BLOCKTYPE type) throws IOException {
            messageQueue.add("world.setBlock(" + x + "," + y + "," + z + "," + type.getValue() + ")");
        }

        public int readBlock(int x, int y, int z) throws IOException {
            messageQueue.add("world.getBlock(" + x + "," + y + "," + z + ")");
            String result = readFromSocket();
            //TODO parse result
            Log.w("minecrat", result);
            return 0;
        }

        public void clearSpecificArea(int x, int y, int z, int x2, int y2, int z2, BLOCKTYPE type) throws IOException {
            writeBlocks(x, y, z, x2, y2, z2, type);
        }

        private void clearArea() throws IOException {
            writeBlocks(XMIN, YMIN, ZMIN, XMAX, YMAX, ZMAX, BLOCKTYPE.CLEAR);
        }

        private void notifyRecievers() {
            for (String message : results) {
                for (MinecraftSubscriber subscriber : subscribers) {
                    subscriber.publish(message);
                }
            }
        }

        public void addSubscriber(MinecraftSubscriber subscriber) {
            subscribers.add(subscriber);
        }

        public void clearSubscribers() {
            subscribers.clear();
        }

        public void shutdown() {
            finished = true;
            clearSubscribers();
        }
    }

    @Override
    public void run() {
        connectToSocket();
        while (!stop) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


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


}
