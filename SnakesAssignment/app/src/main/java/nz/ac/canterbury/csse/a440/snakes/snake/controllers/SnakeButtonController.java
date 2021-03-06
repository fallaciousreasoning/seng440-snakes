package nz.ac.canterbury.csse.a440.snakes.snake.controllers;

import android.view.View;
import android.widget.Button;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;

/**
 * A method of controlling the snake through buttons
 */
public class SnakeButtonController implements SnakeController {
    private Direction direction = Direction.NORTH;

    /**
     * Binds a button to a direction for the snake
     *
     * @param button       The button to bind to
     * @param newDirection The new direction for the snake
     */
    public void bindButton(Button button, final Direction newDirection) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = newDirection;
            }
        });
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void reset(Direction direction) {
        this.direction = direction;
    }
}
