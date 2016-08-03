package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * An open gl renderer for the snake game
 */
public class SnakeGLView extends GLSurfaceView implements Renderer {
    private SnakeGLRenderer renderer;
    private SnakeGame snakeGame;

    public SnakeGLView(Context context) {
        super(context);

        snakeGame = new SnakeGame(20, 30, 20, 3);

        setEGLContextClientVersion(2);

        renderer = new SnakeGLRenderer();
        setRenderer(renderer);
    }

    @Override
    public void render(SnakeGame game) {
        this.snakeGame = game;
        renderer.render(game);

        requestRender();
    }
}
