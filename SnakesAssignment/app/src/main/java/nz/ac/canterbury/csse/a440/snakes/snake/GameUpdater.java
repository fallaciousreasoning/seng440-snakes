package nz.ac.canterbury.csse.a440.snakes.snake;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles updating the snake game
 */
public class GameUpdater {
    /**
     * The number of times per second the snake should be updated
     */
    private int updateRate = (int)(1000.0/1.0);

    /**
     * The snake game to be updated by the updater
     */
    private final SnakeGame game;

    /**
     * A timer to handle ticking the game clock
     */
    private Timer updateTimer = new Timer();

    /**
     * A handler for passing information to the GUI (you can't update the GUI unless you're
     * on the GUI thread
     */
    private Handler updateHandler = new Handler();

    public GameUpdater(final SnakeGame game){
        this.game = game;

        updateTimer = new Timer();
        schedule();
    }

    /**
     * Sets the update rate for the game
     * @param updateRate The update rate (in milliseconds)
     */
    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
        updateTimer.cancel();

        schedule();
    }

    /**
     * Resets the timer
     */
    private void schedule() {
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //TODO I'd like to be using a lambda here, but I can't work out how to configure JACK
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        game.step();
                    }
                });
            }
        }, updateRate, updateRate);
    }
}
