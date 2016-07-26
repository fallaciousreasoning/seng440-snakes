package nz.ac.canterbury.csse.a440.snakes;

import org.junit.Before;
import org.junit.Test;

import nz.ac.canterbury.csse.a440.snakes.snake.Snake;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

import static org.junit.Assert.*;

/**
 * Created by jayha on 26/07/2016.
 */
public class SnakeTest {
    private Snake snake;

    @Before
    public void setup() {
        snake = new Snake(new Vector3(0, 0), new Vector3(1, 0), 1, 3);

        assertEquals(new Vector3(0, 0), snake.headPosition());
        assertEquals(new Vector3(1, 0), snake.stepAmount());
        assertEquals(3, snake.length());
    }

    @Test
    public void testGetHead() {
        Vector3 expectedHead = snake.headPosition().add(snake.stepAmount());

        snake.step();
        assertEquals(expectedHead, snake.headPosition());

        expectedHead = snake.headPosition().add(snake.stepAmount());
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
            Vector3 expectedHead = snake.headPosition().add(snake.stepAmount());

            snake.step();
            assertEquals(expectedHead, snake.headPosition());

            assertEquals(length + Math.min(i + 1, grow), snake.length());
        }
    }
}
