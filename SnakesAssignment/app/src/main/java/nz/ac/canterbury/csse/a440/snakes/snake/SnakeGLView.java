package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import nz.ac.canterbury.csse.a440.snakes.R;

/**
 * An open gl renderer for the snake game
 */
public class SnakeGLView extends GLSurfaceView implements Renderer {
    private SnakeGLRenderer renderer;
    private SnakeGame snakeGame;

    public SnakeGLView(Context context) {
        super(context);
        init(null, 1);
    }

    public SnakeGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 1);
    }

    public SnakeGLView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CanvasViewRenderer,
                0, 0);

        snakeGame = new SnakeGame(20, 30, 20, 3);
        snakeGame.addRenderer(this);

        setEGLContextClientVersion(2);

        renderer = new SnakeGLRenderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        requestRender();
    }

    @Override
    public void render(SnakeGame game) {
        this.snakeGame = game;
        renderer.render(game);

        requestRender();
    }
}
