package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * A piece of food for the snake to eat
 */
public class Food {
    private int growth = 1;
    private Vector3 position;

    public Food(int growth, Vector3 position) {
        this.growth = growth;
        this.position = position;
    }

    /**
     * The amount the food causes the snake to grow
     *
     * @return The amount the food causes the snake to grow
     */
    public int getGrowth() {
        return growth;
    }

    /**
     * The position of the food
     *
     * @return The position of the food
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Sets the position of the food. Used in testing.
     *
     * @param position The position of the food
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
