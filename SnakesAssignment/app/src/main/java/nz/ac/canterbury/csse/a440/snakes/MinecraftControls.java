package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import nz.ac.canterbury.csse.a440.snakes.minecraft.SSLClient;
import nz.ac.canterbury.csse.a440.snakes.snake.MinecraftSubscriber;

/**
 * Created by wooll on 09-Aug-16.
 */
public class MinecraftControls implements Runnable {

    private static final String defaultToken = "03e699db46ee679c79277f11368aa289";
    public static final int XMAX = 100;
    public static final int XMIN = 0;
    public static final int YMIN = 0;
    public static final int YMAX = 100;
    public static final int ZMIN = 0;
    public static final int ZMAX = 100;
    private final SSLClient sslClient;
    private String url;
    private int port;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MinecraftSubscriber> subscribers = new ConcurrentLinkedQueue<>();
    private boolean stop = false;
    private Thread socketReaderThread;
    private SocketReader socketReader;

    public MinecraftControls(Context context) {
        sslClient = new SSLClient(context, "password".toCharArray());
    }

    private void connectToSocket() {
        try {
            socket = sslClient.getSocket();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writeToSocket(defaultToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SocketReader implements Runnable {
        public String readFromSocket() throws IOException {
            if (reader != null) {
                StringBuilder output = new StringBuilder();
                int read;
                while ((read = reader.read()) != -1) {
                    if (read != 10) {
                        output.append(read - 48);
                    }
                    else {
                        return output.toString();
                    }
                }
                return output.toString();
            }
            return "";
        }

        @Override
        public void run() {
            while (!stop || !messageQueue.isEmpty()) {
                try {
                    String result = readFromSocket();
                    if (result.length() > 0) {
                        notifyRecievers(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public void writeBlocks(int x, int y, int z, int x2, int y2, int z2, BLOCKTYPE type) throws IOException {
        messageQueue.add("world.setBlocks(" + x + "," + y + "," + z + "," + x2 + "," + y2 + "," + z2 + "," + type.getValue() + ")");
    }

    public void writeBlock(int x, int y, int z, BLOCKTYPE type) throws IOException {
        messageQueue.add("world.setBlock(" + x + "," + y + "," + z + "," + type.getValue() + ")");
        readBlock(x, y, z);
    }

    public void readBlock(int x, int y, int z) throws IOException {
        messageQueue.add("world.getBlock(" + x + "," + y + "," + z + ")");
    }

    public void clearSpecificArea(int x, int y, int z, int x2, int y2, int z2, BLOCKTYPE type) throws IOException {
        writeBlocks(x, y, z, x2, y2, z2, type);
    }

    private void clearArea() throws IOException {
        writeBlocks(XMIN, YMIN, ZMIN, XMAX, YMAX, ZMAX, BLOCKTYPE.CLEAR);
    }

    private void notifyRecievers(String message) {
        for (MinecraftSubscriber subscriber : subscribers) {
            subscriber.publish(message);
        }
    }

    public void addSubscriber(MinecraftSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void clearSubscribers() {
        subscribers.clear();
    }


    @Override
    public void run() {
        connectToSocket();
        if (socketReader == null) {
            socketReader = new SocketReader();
        }
        if (socketReaderThread == null) {
            socketReaderThread = new Thread(socketReader);
        }
        socketReaderThread.start();
        try {
            while (!stop || !messageQueue.isEmpty()) {
                if (!messageQueue.isEmpty()) {
                    writeToSocket(messageQueue.poll());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
