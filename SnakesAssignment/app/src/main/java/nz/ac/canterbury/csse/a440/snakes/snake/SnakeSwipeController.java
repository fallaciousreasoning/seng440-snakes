package nz.ac.canterbury.csse.a440.snakes.snake;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * A simple listener for detecting swipe gestures
 */
public class SnakeSwipeController extends GestureDetector.SimpleOnGestureListener implements SnakeController{
    private Direction direction;

    public SnakeSwipeController(SnakeGame game) {
        direction = game.getSnake().getDirection();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //We're moving east or west
        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            //Negative is east, positive is west
            direction = distanceX < 0 ? Direction.EAST : Direction.WEST;
        }
        //We're moving north or south
        else {
            //Negative is south, positive is north
            direction = distanceY < 0 ? Direction.SOUTH : Direction.NORTH;
        }
        return true;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
