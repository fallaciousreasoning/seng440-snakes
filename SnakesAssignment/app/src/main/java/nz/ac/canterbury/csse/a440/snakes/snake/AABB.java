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

    /**
     * Determines whether an AABB intersects this one
     * @param other The AABB to check for intersection with
     * @return Whether the boxes intersect
     */
    public boolean intersects(AABB other) {
        //Check if the boxes don't overlap on any axis. If they don't we know there is no overlap
        return !(
                        max.getX() < other.min.getX() ||
                        min.getX() > other.max.getX() ||

                        max.getY() < other.min.getY() ||
                        min.getY() > other.max.getY() ||

                        max.getZ() < other.min.getZ() ||
                        min.getZ() > other.max.getZ()
        );
    }

    /**
     * Checks to see if the AABB contains a point
     * @param point The point
     * @return whether the AABB contains it
     */
    public boolean contains(Vector3 point) {
        return point.getX() >= min.getX() && point.getX() <= max.getX() &&
                point.getY() >= min.getY() && point.getY() <= max.getY() &&
                point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }
}
