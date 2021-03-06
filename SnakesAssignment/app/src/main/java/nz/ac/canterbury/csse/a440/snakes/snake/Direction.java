package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * An enum for relating directions to vectors.
 * NORTH/SOUTH: Y axis
 * EAST/WEST: X axis
 * UP/DOWN: Z axis
 */
public enum Direction {
    NORTH(new Vector3(0, -1, 0)),
    SOUTH(new Vector3(0, 1, 0)),
    EAST(new Vector3(1, 0, 0)),
    WEST(new Vector3(-1, 0, 0)),
    UP(new Vector3(0, 0, -1)),
    DOWN(new Vector3(0, 0, 1));

    /**
     * A vector representation of the direction
     */
    private Vector3 direction;

    /**
     * Instantiates a new direction
     *
     * @param direction The direction vector
     */
    Direction(Vector3 direction) {
        this.direction = direction;
    }

    /**
     * Gets the vector representing the direction
     *
     * @return The direction vector
     */
    public Vector3 getDirection() {
        return direction;
    }
}
