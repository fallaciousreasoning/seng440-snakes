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

    private int startingLength = 3;

    @Before
    public void setup() {
        snakeGame = new SnakeGame(20, 30, 1, startingLength);

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
    }

    @Test
    public void testSetupLength() {
        assertEquals(startingLength, snakeGame.getSnake().length());
    }

    @Test
    public void testSetupScore() {
        assertEquals(0, snakeGame.score());
    }

    @Test
    public void testSetupStartLength() {
        assertEquals(startingLength, snakeGame.startingLength());
    }

    @Test
    public void testSetupNotStarted() {
        assertFalse(snakeGame.started());
    }

    @Test
    public void testCantMoveBeforeStart() {
        Vector3 position = snakeGame.getSnake().headPosition();
        snakeGame.step();

        assertEquals(position, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testCanMoveOnceStarted() {
        Vector3 position = snakeGame.getSnake().headPosition();

        snakeGame.start();
        snakeGame.step();

        assertNotEquals(position, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testResetSetsHeadPosition() {
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

        //Step for fun
        snakeGame.step();

        snakeGame.reset();

        assertEquals(headPosition, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testResetSetsScore() {
        int score = snakeGame.score();

        snakeGame.start();

        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition());

        //Step to eat
        snakeGame.step();

        //Put the food somewhere we can't get it
        snakeGame.getFood().setPosition(new Vector3(0, 0, 0));

        //Step to grow
        snakeGame.step();

        //Step for fun
        snakeGame.step();

        snakeGame.reset();
        assertEquals(score, snakeGame.score());
    }

    @Test
    public void testResetSetsStartingLength() {
        snakeGame.start();

        snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition());

        //Step to eat
        snakeGame.step();

        //Put the food somewhere we can't get it
        snakeGame.getFood().setPosition(new Vector3(0, 0, 0));

        //Step to grow
        snakeGame.step();

        //Step for fun
        snakeGame.step();

        snakeGame.reset();
        assertEquals(snakeGame.startingLength(), snakeGame.getSnake().length());
    }

    @Test
    public void testMove() {
        Vector3 startPosition = snakeGame.getSnake().headPosition();

        snakeGame.start();

        Direction[] directions = new Direction[] {
            Direction.WEST,
            Direction.SOUTH,
            Direction.EAST,
            Direction.NORTH
        };

        for (Direction direction : directions) {
            this.direction = direction;

            Vector3 expectedPosition = snakeGame.getSnake().stepAmount(direction).add(snakeGame.getSnake().headPosition());

            snakeGame.step();

            assertEquals(expectedPosition, snakeGame.getSnake().headPosition());
        }
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
            }

            snakeGame.step();
            assertTrue(snakeGame.hitWall());
            snakeGame.reset();
        }
    }

    @Test
    public void testCantMoveOnHitWall() {
        for (Direction direction : Direction.values()) {
            snakeGame.start();

            if (snakeGame.getSnake().isBackwards(direction)) {
                this.direction = Direction.WEST;
                snakeGame.step();
            }

            this.direction = direction;

            while (snakeGame.getBounds().contains(snakeGame.getSnake().nextPosition(this.direction))) {
                snakeGame.step();
            }

            snakeGame.step();
            //Now, we should have hit a wall

            Vector3 position = snakeGame.getSnake().headPosition();
            snakeGame.step();
            assertEquals(position, snakeGame.getSnake().headPosition());

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

        direction = Direction.WEST;
        snakeGame.step();

        direction = Direction.SOUTH;
        snakeGame.step();

        direction = Direction.EAST;
        snakeGame.step();

        assertTrue(snakeGame.hitSelf());
    }

    @Test
    public void testCantMoveOnHitSelf() {
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

        direction = Direction.WEST;
        snakeGame.step();

        direction = Direction.SOUTH;
        snakeGame.step();

        direction = Direction.EAST;
        snakeGame.step();

        //At this point we should have hit ourselves and should not be able to move

        Vector3 position = snakeGame.getSnake().headPosition();

        direction = Direction.SOUTH;
        snakeGame.step();

        assertEquals(position, snakeGame.getSnake().headPosition());
    }

    @Test
    public void testEatFoodIncreasesScore() {
        snakeGame.start();

        int eat = 3;

        for (int i = 0; i < eat; ++i){
            snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition(direction));
            snakeGame.step();
        }

        snakeGame.step();
        assertEquals(eat, snakeGame.score());
    }

    @Test
    public void testEatFoodIncreasesLength() {
        snakeGame.start();

        int eat = 3;

        for (int i = 0; i < eat; ++i){
            snakeGame.getFood().setPosition(snakeGame.getSnake().nextPosition(direction));
            snakeGame.step();
        }

        snakeGame.step();
        assertEquals(snakeGame.startingLength() + eat, snakeGame.getSnake().length());
    }
}
