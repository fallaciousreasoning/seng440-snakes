package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * A controller for the snake that makes use of accelerometer data
 */
public class SnakeAccelerometerController implements SnakeController {
    private Direction direction = Direction.NORTH;
    @Override
    public Direction getDirection() {
        return direction;
    }
}
