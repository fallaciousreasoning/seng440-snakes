package nz.ac.canterbury.csse.a440.snakes.snake.controllers;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;

/**
 * A controller which takes a normal input method and adds a z control
 */
public class SnakeAlmost3DController implements SnakeController{
    private SnakeController xyController;
    private SnakeController zController;

    private Direction lastXYDirection;
    private Direction lastZDirection;

    private boolean onZ;

    public SnakeAlmost3DController(SnakeController xyController, SnakeController zController) {
        this.xyController = xyController;
        this.zController = zController;
    }

    @Override
    public Direction getDirection() {
        Direction xyDirection = xyController.getDirection();
        Direction zDirection = zController.getDirection();

        if (xyDirection != Direction.UP && xyDirection != lastXYDirection) {
            zController.reset(Direction.NORTH);
            lastXYDirection = xyDirection;
            lastZDirection = zController.getDirection();
            onZ = false;
        }

        if (zDirection != Direction.NORTH && zDirection != lastZDirection) {
            xyController.reset(Direction.UP);
            lastXYDirection = xyController.getDirection();

            lastZDirection = zDirection;
            onZ = true;
        }

        return onZ ? zController.getDirection() : xyController.getDirection();
    }

    @Override
    public void reset(Direction direction) {
        xyController.reset(direction);
        zController.reset(direction);

        lastXYDirection = xyController.getDirection();
        lastZDirection = zController.getDirection();
    }

    public void setXYController(SnakeController xyController) {
        this.xyController = xyController;
    }

    public void setZController(SnakeController zController) {
        this.zController = zController;
    }
}
