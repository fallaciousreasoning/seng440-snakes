package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import nz.ac.canterbury.csse.a440.snakes.MinecraftControls;

/**
 * Created by wooll on 10-Aug-16.
 */
public class SnakeMinecraftRenderer implements Renderer {

    private final Context context;
    private final SnakeRendererState state;

    public SnakeMinecraftRenderer(Context context) {
        this.context = context;
        state = new SnakeRendererState();
    }

    @Override
    public void render(SnakeGame game) {
        state.setGame(game);
        new SnakeMinecraftTask(context).execute(state);
    }
}
