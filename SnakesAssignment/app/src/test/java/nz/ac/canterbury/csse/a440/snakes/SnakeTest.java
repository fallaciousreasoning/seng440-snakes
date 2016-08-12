package nz.ac.canterbury.csse.a440.snakes;

import org.junit.Before;
import org.junit.Test;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.Snake;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

import static org.junit.Assert.*;

/**
 * Created by jayha on 26/07/2016.
 */
public class SnakeTest {
    private Snake snake;
    private Vector3 startPosition;
    private int startLength;

    @Before
    public void setup() {
        startPosition = Vector3.Zero;
        this.startLength = 3;
        snake = new Snake(startPosition, Direction.EAST, 1, startLength);
    }

    @Test
    public void testSetupHeadPosition() {
        assertEquals(startPosition, snake.headPosition());
    }

    @Test
    public void testSetupStepAmount() {
        assertEquals(new Vector3(1, 0), snake.stepAmount());
    }

    @Test
    public void testSetupLength() {

        assertEquals(startLength, snake.length());
    }

    @Test
    public void testGetHead1() {
        Vector3 expectedHead = snake.headPosition().add(snake.stepAmount());

        snake.step();
        assertEquals(expectedHead, snake.headPosition());
    }

    @Test
    public void testGetHead2() {
        Vector3 expectedHead = snake.headPosition().add(snake.stepAmount().mul(2));

        snake.step();
        snake.grow();
        snake.step();
        assertEquals(expectedHead, snake.headPosition());
    }

    @Test
    public void testGrow() {
        int length = snake.length();
        int grow = 5;
        int tests = 7;

        snake.grow(grow);

        for (int i = 0; i < tests; ++i){
            snake.step();

            assertEquals(length + Math.min(i + 1, grow), snake.length());
        }
    }
}
