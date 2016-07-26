package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * Created by jayha on 26/07/2016.
 */
public class Food {
    private int growth = 1;
    private Vector3 position;

    /**
     * The amount the food causes the snake to grow
     * @return The amount the food causes the snake to grow
     */
    public int getGrowth() {
        return growth;
    }

    /**
     * The position of the food
     * @return The position of the food
     */
    public Vector3 getPosition() {
        return position;
    }
}
