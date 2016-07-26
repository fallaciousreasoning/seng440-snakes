package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A class representing a snake in the game
 */
public class Snake {
    /**
     * The size of the blocks the snake consists of
     */
    private final float blockSize;

    /**
     * The points making up a snake
     */
    private LinkedList<Vector3> points = new LinkedList<>();

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
     * @param headPosition The initial head position of the snake
     * @param headingDirection The initial direction the snake is heading
     * @param blockSize The size of the blocks the snake is made up of
     * @param length The initial length of the snake
     */
    public Snake(Vector3 headPosition, Direction headingDirection, float blockSize, int length) {
        this.blockSize = blockSize;
        this.direction = headingDirection;

        Vector3 tailDirection = stepAmount().mul(-1);

        points.add(headPosition);

        for (int i = 0; i < length - 1; ++i){
            Vector3 nextPos = points.get(points.size() - 1).add(tailDirection);
            points.add(nextPos);
        }
    }

    /**
     * Gets the amount that the snake will be moved by
     * @return The amount the snake will be moved by
     */
    public Vector3 stepAmount() {
        return direction.getDirection().mul(blockSize);
    }

    /**
     * Gets the position of the head of the snake
     * @return The position of the head of the snake
     */
    public Vector3 headPosition() {
        return points.get(0);
    }

    /**
     * Gets the next position that the head will be at
     * @return The next position the head will be at
     */
    public Vector3 nextPosition() {
        return headPosition().add(stepAmount());
    }

    /**
     * The new direction for the snake to head in.
     * @param direction The new direction for the snake
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Makes the snake take a step
     */
    public void step() {
        //If the snake hasn't eaten, we should remove the last block (we put a new one
        //on the front each step)
        if (grow <= 0) {
            points.removeLast();
        } else grow--;

        //Get the next position of the snake
        Vector3 nextPos = nextPosition();

        //Set it as our new head position
        points.addFirst(nextPos);
    }

    /**
     * Should be called when the head of the snake encounters some food.
     * @param by The amount to grow the snake by
     */
    public void grow(int by) {
        grow += by;
    }

    /**
     * Grows the snake by one block
     */
    public void grow(){
        grow(1);
    }

    /**
     * Gets the length of the snake
     * @return The length of the snake
     */
    public int length() {
        return points.size();
    }

    /**
     * Indicates whether the snake will intersect itself after moving
     * @return Whether the snake will intersect itself
     */
    public boolean willIntersect() {
        boolean first = false;
        Vector3 nextPosition = nextPosition();

        for (Vector3 position : points) {
            //Skip the head. I'd like to do this with a stream but it doesn't exist
            if (first) {
                first = true;
                continue;
            }

            //Check if this position is part of the snake
            if (nextPosition.equals(position)) {
                //If it is, we're done!
                return true;
            }
        }

        //If we didn't find anything, we're all good
        return false;
    }
}
