package nz.ac.canterbury.csse.a440.snakes;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.InputMethod;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;

    private SnakeGame game;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        game = activity.game;

        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.actuallyUse = InputMethod.BUTTONS;
                    activity.setupControls();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        game.setStarted(true);
        activity.updater.setUpdateRate(Integer.MAX_VALUE);
    }

    private void changeDirection(Direction direction) {
        activity.controlButtons.get(direction).callOnClick();
    }

    @UiThreadTest
    public void testWestGoodCase() {
        game.reset();
        game.setStarted(true);

        Direction direction = Direction.WEST;
        Vector3 expected = game.getSnake().nextPosition(direction);
        changeDirection(direction);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testSouthGoodCase() {
        game.reset();
        game.setStarted(true);

        //Go east first (we can't go south when aimed north)
        changeDirection(Direction.EAST);
        game.step();

        Direction direction = Direction.SOUTH;
        Vector3 expected = game.getSnake().nextPosition(direction);
        changeDirection(direction);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testEastGoodCase() {
        game.reset();
        game.setStarted(true);

        Direction direction = Direction.EAST;
        Vector3 expected = game.getSnake().nextPosition(direction);
        changeDirection(direction);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testNorthGoodCase() {
        game.reset();
        game.setStarted(true);

        Direction direction = Direction.NORTH;
        Vector3 expected = game.getSnake().nextPosition(direction);
        changeDirection(direction);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testWestBadCase() {
        game.reset();
        game.setStarted(true);

        //Go east first (we can't go south when aimed north)
        changeDirection(Direction.EAST);
        game.step();

        Vector3 expected = game.getSnake().nextPosition(Direction.EAST);
        changeDirection(Direction.WEST);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testSouthBadCase() {
        game.reset();
        game.setStarted(true);

        Vector3 expected = game.getSnake().nextPosition(Direction.NORTH);
        changeDirection(Direction.SOUTH);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testEastBadCase() {
        game.reset();
        game.setStarted(true);

        //Go east first (we can't go south when aimed north)
        changeDirection(Direction.WEST);
        game.step();

        Vector3 expected = game.getSnake().nextPosition(Direction.WEST);
        changeDirection(Direction.EAST);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
    }

    @UiThreadTest
    public void testNorthBadCase() {
        game.reset();
        game.setStarted(true);


        changeDirection(Direction.EAST);
        game.step();

        //Go south (we can't go south when aimed north)
        changeDirection(Direction.SOUTH);
        game.step();

        Direction direction = Direction.SOUTH;
        Vector3 expected = game.getSnake().nextPosition(Direction.SOUTH);
        changeDirection(Direction.NORTH);

        game.step();
        assertEquals(expected, game.getSnake().headPosition());
;
    }
}
