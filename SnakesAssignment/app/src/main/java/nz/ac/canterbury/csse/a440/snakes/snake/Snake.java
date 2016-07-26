package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jayha on 26/07/2016.
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
     * The direction the snake is heading
     */
    private Vector3 headingDirection;

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
    public Snake(Vector3 headPosition, Vector3 headingDirection, float blockSize, int length) {
        this.blockSize = blockSize;

        Vector3 tailDirection =
                new Vector3(0, -blockSize)
                .mul(headingDirection);

        points.add(headPosition);

        for (int i = 0; i < length; ++i){
            Vector3 nextPos = points.get(points.size() - 1).add(tailDirection);
            points.add(nextPos);
        }
    }

    /**
     * Gets the position of the head of the snake
     * @return The position of the head of the snake
     */
    public Vector3 headPosition() {
        return points.get(0);
    }

    /**
     * The new direction for the snake to head in. This should be a unit vector
     * along the x, y or z axis.
     * @param newDirection The new direction for the snake
     */
    public void setHeadingDirection(Vector3 newDirection) {
        this.headingDirection = newDirection;
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

        //Calculate the size of our step
        Vector3 step = headingDirection.mul(blockSize);
        //Add it to the current head of the snake
        Vector3 nextPos = headPosition().add(step);

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
}
