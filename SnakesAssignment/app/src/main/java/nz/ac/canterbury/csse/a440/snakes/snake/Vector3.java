package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.Objects;

/**
 * A class representing a point in 3D space and various
 * helper methods for mathematical operations
 */
public class Vector3 {
    private final float x;
    private final float y;
    private final float z;

    /**
     * Creates a point from an x and y coordinate
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Vector3(float x, float y) {
        this(x, y, 0);
    }

    /**
     * Creates a point from an x, y and z coordinate
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the x coordinate of the vector
     * @return The x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the vector
     * @return The y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the z coordinate of the vector
     * @return The z coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the addition of this vector3 and another
     * @param vector3 The vector3 to add
     * @return A new vector3 representing the combination
     */
    public Vector3 add(Vector3 vector3) {
        return new Vector3(x + vector3.x, y + vector3.y, z + vector3.z);
    }

    /**
     * Returns the subtraction of another vector3 from this one
     * @param vector3 The vector3 to subtract
     * @return A new vector3 representing the combination
     */
    public Vector3 sub(Vector3 vector3) {
        return new Vector3(x - vector3.x, y - vector3.y, z - vector3.z);
    }

    /**
     * Returns the multiplication of this vector3 and another
     * @param vector3 The vector3 to multiply
     * @return A new vector3 representing the combination
     */
    public Vector3 mul(Vector3 vector3) {
        return new Vector3(x * vector3.x, y * vector3.y, z * vector3.z);
    }

    /**
     * Returns the multiplication of the vector by a scaler
     * @param multiplier The scaler
     * @return The scaled vector
     */
    public Vector3 mul(float multiplier) {
        return new Vector3(x * multiplier, y * multiplier, z * multiplier);
    }

    /**
     * Returns the division of this vector3 by another another
     * @param vector3 The vector3 to divide by
     * @return A new vector3 representing the combination
     */
    public Vector3 div(Vector3 vector3) {
        return new Vector3(x / vector3.x, y / vector3.y, z / vector3.z);
    }

    /**
     * Divides the vector by a scaler
     * @param by The scaler
     * @return The scaled vector
     */
    public Vector3 div(float by) {
        return new Vector3(x / by, y / by, z / by);
    }

    /**
     * Returns the length squared of the point. Length squared is provided because square rooting
     * is slow and you can do a fair bit without it
     * @return The squared length of the vector
     */
    public float lengthSquared() {
        return x*x + y*y + z*z;
    }

    /**
     * Gets the length of the vector
     * @return The length of the vector
     */
    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    /**
     * Returns a normalized form of this vector
     * @return The normalized vector
     */
    public Vector3 normalized(){
        return this.div(length());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Vector3)) return false;

        final float epsilon = 0.00001f;

        Vector3 o = (Vector3)other;
        return (x - o.x) < epsilon && (y - o.y) < epsilon && (z - o.z) < epsilon;
    }

    @Override
    public int hashCode() {
        return (int)(x + y + z);
    }

    @Override
    public String toString() {
        return "Vector3: [X: " + x + ", Y: " + y + ", Z: " + z + "]";
    }
}
