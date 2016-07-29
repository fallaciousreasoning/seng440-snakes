package nz.ac.canterbury.csse.a440.snakes.snake;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * A gesture listener that starts or restarts the game with a tap
 */
public class StartFinishGestureListener extends GestureDetector.SimpleOnGestureListener {
    /**
     * The snake game
     */
    private SnakeGame game;

    /**
     * Constructs a new start finish gesture listener for a game
     * @param game The game
     */
    public StartFinishGestureListener(SnakeGame game) {
        this.game = game;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (!game.started())
            game.start();
        if (game.finished())
            game.reset();

        return true;
    }
}
