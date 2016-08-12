package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import nz.ac.canterbury.csse.a440.snakes.MinecraftControls;

/**
 * Created by wooll on 10-Aug-16.
 */
public class SnakeMinecraftRenderer implements Renderer, MinecraftSubscriber {

    private final SnakeRendererState state;
    private MinecraftControls.Messaging messaging;

    public SnakeMinecraftRenderer(Context context) {
        state = new SnakeRendererState();
        MinecraftControls minecraftControls = new MinecraftControls(context);
        Thread minecraftConnectionThread = new Thread(minecraftControls);
        minecraftConnectionThread.start();
        messaging = minecraftControls.getMessaging();
        messaging.addSubscriber(this);
        Thread messagingThread = new Thread(messaging);
        messagingThread.start();
    }

    @Override
    public void render(SnakeGame game) {
        state.setGame(game);
        Vector3 lastKnownTail = state.getLastKnownTail();
        Vector3 lastKnownFood = state.getLastKnownFood();
        Vector3 lastKnownHead = state.getLastKnownHead();

        try {
            if (lastKnownTail != null) {
                // new Food spawned
                Vector3 newFood = game.getFood().getPosition();
                if (!lastKnownFood.equals(newFood)) {
                    messaging.writeBlock((int) newFood.getX(), (int) newFood.getY(), (int) newFood.getZ(), MinecraftControls.BLOCKTYPE.FOOD);
                }

                // move tail if tail position changed, if tail has not changed, then snake has grown
                if (!lastKnownTail.equals(game.getSnake().tailPosition())) {
                    // clear old tail
                    messaging.writeBlock((int) lastKnownTail.getX(), (int) lastKnownTail.getY(), (int) lastKnownTail.getZ(), MinecraftControls.BLOCKTYPE.CLEAR);
                }

                // move head
                Vector3 newHead = game.getSnake().headPosition();
                messaging.writeBlock((int) newHead.getX(), (int) newHead.getY(), (int) newHead.getZ(), MinecraftControls.BLOCKTYPE.HEAD);
                messaging.writeBlock((int) lastKnownHead.getX(), (int) lastKnownHead.getY(), (int) lastKnownHead.getZ(), MinecraftControls.BLOCKTYPE.BODY);


            } else {
                // first time render, render all positions
                // clear old game
                messaging.clearSpecificArea(MinecraftControls.XMIN, MinecraftControls.YMIN, MinecraftControls.ZMIN, (int) game.getBounds().getWidth(), (int) game.getBounds().getHeight(), (int) game.getBounds().getDepth(), MinecraftControls.BLOCKTYPE.CLEAR);
                // render body
                for (Vector3 block : game.getSnake().getPositions()) {
                    messaging.writeBlock((int) block.getX(), (int) block.getY(), (int) block.getZ(), MinecraftControls.BLOCKTYPE.BODY);
                }
                // render head
                Vector3 head = game.getSnake().headPosition();
                messaging.writeBlock((int) head.getX(), (int) head.getY(), (int) head.getZ(), MinecraftControls.BLOCKTYPE.HEAD);

                // render food
                Vector3 food = game.getFood().getPosition();
                messaging.writeBlock((int) food.getX(), (int) food.getY(), (int) food.getZ(), MinecraftControls.BLOCKTYPE.FOOD);

            }
            state.setLastKnownTail(game.getSnake().tailPosition());
            state.setLastKnownFood(game.getFood().getPosition());
            state.setLastKnownHead(game.getSnake().headPosition());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String message) {
        Log.e("minecraft", message);
    }

    public void shutdown() {
        messaging.shutdown();
    }
}
