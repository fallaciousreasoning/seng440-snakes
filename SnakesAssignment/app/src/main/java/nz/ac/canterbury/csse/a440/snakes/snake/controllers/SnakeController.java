package nz.ac.canterbury.csse.a440.snakes.snake.controllers;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;

/**
 * An interface for controlling the snake.
 */
public interface SnakeController {
    /**
     * The direction the snake should move next time the game is stepped. The snake will only move
     * in this direction if 'step' is called on the game.
     *
     * @return The direction the snake should move in
     */
    Direction getDirection();

    /**
     * Tells the controller to set its direction to the default
     * @param direction The direction
     */
    void reset(Direction direction);
}
