package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * Represents an axis aligned bounding box
 */
public class AABB {
    private Vector3 min;
    private Vector3 max;

    /**
     * Gets the width of the AABB
     * @return the width of the AABB
     */
    public float getWidth() {
        return max.getX() - min.getX();
    }

    /**
     * gets the height of the bounding box
     * @return The height of the bounding box
     */
    public float getHeight() {
        return max.getY() - min.getY();
    }

    /**
     * Gets the depth of the bounding box
     * @return The depth of the bounding box
     */
    public float getDepth() {
        return max.getZ() - min.getZ();
    }

    /**
     * Gets the centre of the bounding box
     * @return The centre of the bounding box
     */
    public Vector3 getCentre() {
        return min
                .add(max)
                .div(2);
    }

    /**
     * Gets the minimum coordinate of the bounding box
     * @return The minimum coordinate
     */
    public Vector3 getMin() {
        return min;
    }

    /**
     * Gets the maximum coordinate of the bounding box
     * @return the maximum coordinate of the bounding box
     */
    public Vector3 getMax() {
        return max;
    }
}
