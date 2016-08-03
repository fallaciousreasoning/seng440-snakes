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
    private Direction direction = Direction.NORTH;

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
        snakeGame.step();
        assertNotEquals(position, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testReset() {
        Vector3 headPosition = snakeGame.getSnake().headPosition();
        int length = snakeGame.getSnake().length();
        int score = snakeGame.score();

        snakeGame.start();

        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition());

        //Step to eat
        snakeGame.step();

        //Put the food somewhere we can't get it
        snakeGame.getFood().setPosition(new Vector3(0, 0, 0));

        //Step to grow
        snakeGame.step();

        assertEquals(length + 1, snakeGame.getSnake().length());
        assertEquals(score + 1, snakeGame.score());

        //Step for fun
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
        assertEquals(startPosition.add(snakeGame.getSnake().stepAmount(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.SOUTH;
        snakeGame.step();
        assertEquals(startPosition.add(snakeGame.getSnake().stepAmount(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        snakeGame.step();
        assertEquals(startPosition.add(snakeGame.getSnake().stepAmount(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.EAST;
        snakeGame.step();
        assertEquals(startPosition.add(snakeGame.getSnake().stepAmount(direction)), snakeGame.getSnake().headPosition());

        startPosition = snakeGame.getSnake().headPosition();
        direction = Direction.NORTH;
        snakeGame.step();
        assertEquals(startPosition.add(snakeGame.getSnake().stepAmount(direction)), snakeGame.getSnake().headPosition());

    }

    @Test
    public void testHitWall() {
        for (Direction direction : Direction.values()) {
            snakeGame.start();

            if (snakeGame.getSnake().isBackwards(direction)) {
                this.direction = Direction.WEST;
                snakeGame.step();
            }

            this.direction = direction;

            while (snakeGame.getBounds().contains(snakeGame.getSnake().nextPosition(this.direction))) {
                snakeGame.step();
                assertFalse(snakeGame.hitWall());
            }

            snakeGame.step();
            assertTrue(snakeGame.hitWall());
            snakeGame.reset();
        }
    }

    @Test
    public void testHitTail() {
        snakeGame.start();

        //We need at least a length of five
        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition(direction));

        //Step to eat
        snakeGame.step();

        //We need at least a length of five
        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition(direction));

        //Step to eat and grow
        snakeGame.step();

        //Step to grow
        snakeGame.step();

        assertEquals(5, snakeGame.getSnake().length());

        direction = Direction.WEST;
        snakeGame.step();

        direction = Direction.SOUTH;
        snakeGame.step();

        direction = Direction.EAST;
        snakeGame.step();

        assertTrue(snakeGame.hitSelf());
    }

    @Test
    public void testEatFood() {

    }
}
