package nz.ac.canterbury.csse.a440.snakes;

import org.junit.Before;
import org.junit.Test;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeController;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

import static org.junit.Assert.*;

/**
 * Created by jayha on 4/08/2016.
 */
public class SnakeGameTest {
    private SnakeGame snakeGame;
    private SnakeController controller;
    private Direction direction;

    @Before
    public void setup() {
        snakeGame = new SnakeGame(20, 30, 1, 3);

        controller = new SnakeController() {
            @Override
            public Direction getDirection() {
                return direction;
            }

            @Override
            public void reset() {
                direction = Direction.NORTH;
            }
        };
        snakeGame.setSnakeController(controller);

        assertEquals(3, snakeGame.getSnake().length());
        assertEquals(0, snakeGame.score());
        assertEquals(3, snakeGame.startingLength());
    }

    @Test
    public void testStart() {
        assertFalse(snakeGame.started());
        Vector3 position = snakeGame.getSnake().headPosition();
        snakeGame.step();

        assertEquals(position, snakeGame.getSnake().headPosition());

        snakeGame.start();
        assertNotEquals(position, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testReset() {
        Vector3 headPosition = snakeGame.getSnake().headPosition();
        int length = snakeGame.getSnake().length();
        int score = snakeGame.score();

        snakeGame.start();

        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition());
        assertEquals(length + 1, snakeGame.getSnake().length());
        assertEquals(score + 1, snakeGame.score());

        snakeGame.step();
        snakeGame.step();
        snakeGame.step();

        assertNotEquals(headPosition, snakeGame.getSnake().headPosition());

        snakeGame.reset();
        assertEquals(headPosition, snakeGame.getSnake().headPosition());
        assertEquals(score, snakeGame.score());
        assertEquals(snakeGame.startingLength(), snakeGame.getSnake().length());
    }

    @Test
    public void testMove() {
        Vector3 startPosition = snakeGame.getSnake().headPosition();

        snakeGame.start();

        direction = Direction.WEST;

        snakeGame.step();
        assertEquals(startPosition.add(snakeGame.getSnake().nextPosition(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.SOUTH;
        assertEquals(startPosition.add(snakeGame.getSnake().nextPosition(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        assertEquals(startPosition.add(snakeGame.getSnake().nextPosition(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.EAST;
        assertEquals(startPosition.add(snakeGame.getSnake().nextPosition(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.NORTH;
        assertEquals(startPosition.add(snakeGame.getSnake().nextPosition(direction)), snakeGame.getSnake().headPosition());

    }

    @Test
    public void testHitWall() {

    }

    @Test
    public void testHitTail() {

    }

    @Test
    public void testEatFood() {

    }
}
