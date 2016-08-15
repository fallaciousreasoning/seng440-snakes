package nz.ac.canterbury.csse.a440.snakes.snake;

import android.view.GestureDetector;
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
     * Sets the game
     *
     * @param game The game
     */
    public void setGame(SnakeGame game) {
        this.game = game;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (game == null) return true;

        if (!game.isStarted())
            game.start();
        if (game.finished())
            game.reset();

        return true;
    }
}
