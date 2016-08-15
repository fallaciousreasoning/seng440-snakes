package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A class representing a snake in the game
 */
public class Snake {
    /**
     * The size of the blocks the snake consists of
     */
    private final float blockSize;

    /**
     * The positions making up a snake
     */
    private ConcurrentLinkedDeque<Vector3> positions = new ConcurrentLinkedDeque<>();

    /**
     * The direction the snake is heading in
     */
    private Direction direction;

    /**
     * The number of blocks the snake needs to grow by
     */
    private int grow;

    /**
     * Constructs a new snake
     *
     * @param headPosition     The initial head position of the snake
     * @param headingDirection The initial direction the snake is heading
     * @param blockSize        The size of the blocks the snake is made up of
     * @param length           The initial length of the snake
     */
    public Snake(Vector3 headPosition, Direction headingDirection, float blockSize, int length) {
        this.blockSize = blockSize;
        this.direction = headingDirection;

        Vector3 tailDirection = stepAmount(headingDirection).mul(-1);

        positions.add(headPosition);

        for (int i = 0; i < length - 1; ++i) {
            Vector3 nextPos = positions.getLast().add(tailDirection);
            positions.add(nextPos);
        }
    }

    /**
     * Gets the amount that the snake will be moved by
     *
     * @return The amount the snake will be moved by
     */
    public Vector3 stepAmount() {
        return stepAmount(direction);
    }

    /**
     * Gets the amount that the snake will be moved by
     *
     * @param direction The direction to step in
     * @return The amount the snake will be moved by
     */
    public Vector3 stepAmount(Direction direction) {
        return direction.getDirection().mul(blockSize);
    }

    /**
     * Gets the position of the head of the snake
     *
     * @return The position of the head of the snake
     */
    public Vector3 headPosition() {
        return positions.getFirst();
    }

    /**
     * Gets the next position that the head will be at
     *
     * @return The next position the head will be at
     */
    public Vector3 nextPosition() {
        return nextPosition(direction);
    }

    /**
     * Gets the next position that the head will be at
     *
     * @param direction The direction the next position is in
     * @return The next position the head will be at
     */
    public Vector3 nextPosition(Direction direction) {
        return headPosition().add(stepAmount(direction));
    }

    /**
     * Gets the direction the snake is currently heading in
     *
     * @return The direction the snake is heading in
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * The new direction for the snake to head in. The direction will not be set if it is backwards
     * (you can't make the snake go backwards)
     *
     * @param direction The new direction for the snake
     */
    public void setDirection(Direction direction) {
        if (isBackwards(direction)) {
            //TODO maybe throw an exception so we know something went wrong?
            return;
        }
        this.direction = direction;
    }

    /**
     * Makes the snake take a step
     */
    public void step() {
        synchronized (this) {
            //If the snake hasn't eaten, we should remove the last block (we put a new one
            //on the front each step)
            if (grow <= 0) {
                positions.removeLast();
            } else grow--;

            //Get the next position of the snake
            Vector3 nextPos = nextPosition();

            //Set it as our new head position
            positions.addFirst(nextPos);
        }
    }

    /**
     * Should be called when the head of the snake encounters some food.
     *
     * @param by The amount to grow the snake by
     */
    public void grow(int by) {
        grow += by;
    }

    /**
     * Grows the snake by one block
     */
    public void grow() {
        grow(1);
    }

    /**
     * Gets the length of the snake
     *
     * @return The length of the snake
     */
    public int length() {
        return positions.size();
    }

    /**
     * Gets a list of positions that the snake is made up of
     *
     * @return The positions
     */
    public Deque<Vector3> getPositions() {
        return positions;
    }

    /**
     * Indicates whether the snake will intersect itself after moving
     *
     * @return Whether the snake will intersect itself
     */
    public boolean willIntersect() {
        Vector3 nextPosition = nextPosition();

        return onSnake(nextPosition, true);
    }

    /**
     * Determines whether a direction would cause the snake to turn 180 degrees
     *
     * @param direction The direction to test
     * @return Whether the direction is backwards
     */
    public boolean isBackwards(Direction direction) {
        //The snake is going backwards if our new next position would be the second block of the snake
        Iterator<Vector3> i = positions.iterator();
        Vector3 head = i.next();
        assert head.equals(headPosition());

        return nextPosition(direction).equals(i.next());
    }

    /**
     * Indicates whether a position is on the snake
     *
     * @param position The position
     * @return Whether it is on the snake
     */
    public boolean onSnake(Vector3 position) {
        return onSnake(position, false);
    }

    /**
     * Indicates whether or not a position is on the snake
     *
     * @param position  The position
     * @param nextFrame Indicates whether we should only care about the next frame
     * @return Whether it is on the snake
     */
    private boolean onSnake(Vector3 position, boolean nextFrame) {
        for (Vector3 snakePosition : positions) {
            //Skip the head. I'd like to do this with a stream but it doesn't exist

            //Check if this position is part of the snake and it isn't the last block if we don't need to grow
            //(the last block will move)
            if (snakePosition.equals(position) && (!positions.getLast().equals(position) || grow > 0 || !nextFrame)) {
                //If it is, we're done!
                return true;
            }
        }

        //If we didn't find anything, we're all good
        return false;
    }

    public Vector3 tailPosition() {
        return positions.getLast();
    }
}
