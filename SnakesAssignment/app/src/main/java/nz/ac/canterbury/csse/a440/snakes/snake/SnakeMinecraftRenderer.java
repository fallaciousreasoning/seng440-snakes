package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;

import java.io.IOException;

import nz.ac.canterbury.csse.a440.snakes.MinecraftControls;

/**
 * Created by wooll on 10-Aug-16.
 */
public class SnakeMinecraftRenderer implements Renderer {

    MinecraftControls minecraftControls;
    Vector3 lastKnownTail;
    Vector3 lastKnownFood;
    private Vector3 lastKnownHead;

    public SnakeMinecraftRenderer(Context context) {
        minecraftControls = new MinecraftControls(context);
    minecraftControls.connectToSocket();
    }

    @Override
    public void render(SnakeGame game) {

        try {

            if (lastKnownTail != null) {
                // new Food spawned
                Vector3 newFood = game.getFood().getPosition();
                if (!lastKnownFood.equals(newFood)){
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


            }
            else {
                // first time render, render all positions
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
            lastKnownTail = game.getSnake().tailPosition();
            lastKnownFood = game.getFood().getPosition();
            lastKnownHead = game.getSnake().headPosition();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        minecraftControls.close();
    }
}
