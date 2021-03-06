package nz.ac.canterbury.csse.a440.snakes.snake.renderers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import nz.ac.canterbury.csse.a440.snakes.MinecraftControls;
import nz.ac.canterbury.csse.a440.snakes.snake.MinecraftSubscriber;
import nz.ac.canterbury.csse.a440.snakes.snake.Renderer;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

/**
 * Created by wooll on 10-Aug-16.
 */
public class SnakeMinecraftRenderer implements Renderer, MinecraftSubscriber {

    private final SnakeRendererState state;
    private final MinecraftControls minecraftControls;

    public SnakeMinecraftRenderer(Context context) {
        state = new SnakeRendererState();
        minecraftControls = new MinecraftControls(context);
        minecraftControls.addSubscriber(this);
        Thread minecraftConnectionThread = new Thread(minecraftControls);
        minecraftConnectionThread.start();
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
                    minecraftControls.writeBlock((int) newFood.getX(), (int) newFood.getY(), (int) newFood.getZ(), MinecraftControls.BLOCKTYPE.FOOD);
                }

                // move tail if tail position changed, if tail has not changed, then snake has grown
                if (!lastKnownTail.equals(game.getSnake().tailPosition())) {
                    // clear old tail
                    minecraftControls.writeBlock((int) lastKnownTail.getX(), (int) lastKnownTail.getY(), (int) lastKnownTail.getZ(), MinecraftControls.BLOCKTYPE.CLEAR);
                }

                // move head
                Vector3 newHead = game.getSnake().headPosition();
                minecraftControls.writeBlock((int) newHead.getX(), (int) newHead.getY(), (int) newHead.getZ(), MinecraftControls.BLOCKTYPE.HEAD);
                minecraftControls.writeBlock((int) lastKnownHead.getX(), (int) lastKnownHead.getY(), (int) lastKnownHead.getZ(), MinecraftControls.BLOCKTYPE.BODY);


            } else {
                // first time render, render all positions
                // clear old game
                minecraftControls.clearSpecificArea(MinecraftControls.XMIN, MinecraftControls.YMIN, MinecraftControls.ZMIN, (int) game.getBounds().getWidth(), (int) game.getBounds().getHeight(), (int) game.getBounds().getDepth(), MinecraftControls.BLOCKTYPE.CLEAR);
                // render body
                for (Vector3 block : game.getSnake().getPositions()) {
                    minecraftControls.writeBlock((int) block.getX(), (int) block.getY(), (int) block.getZ(), MinecraftControls.BLOCKTYPE.BODY);
                }
                // render head
                Vector3 head = game.getSnake().headPosition();
                minecraftControls.writeBlock((int) head.getX(), (int) head.getY(), (int) head.getZ(), MinecraftControls.BLOCKTYPE.HEAD);

                // render food
                Vector3 food = game.getFood().getPosition();
                minecraftControls.writeBlock((int) food.getX(), (int) food.getY(), (int) food.getZ(), MinecraftControls.BLOCKTYPE.FOOD);

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
        minecraftControls.close();
    }
}
