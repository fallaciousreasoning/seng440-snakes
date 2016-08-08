package nz.ac.canterbury.csse.a440.snakes.snake;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * An enum for relating directions to vectors.
 * NORTH/SOUTH: Y axis
 * EAST/WEST: X axis
 * UP/DOWN: Z axis
 */
public enum Direction implements Parcelable {
    NORTH(new Vector3(0, -1, 0)),
    SOUTH(new Vector3(0, 1, 0)),
    EAST(new Vector3(1, 0, 0)),
    WEST(new Vector3(-1, 0, 0)),
    UP(new Vector3(0, 0, -1)),
    DOWN(new Vector3(0, 0, 1));

    /**
     * Instantiates a new direction
     * @param direction The direction vector
     */
    Direction(Vector3 direction) {
        this.direction = direction;
    }

    /**
     * A vector representation of the direction
     */
    private Vector3 direction;

    /**
     * Gets the vector representing the direction
     * @return The direction vector
     */
    public Vector3 getDirection() {
        return direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
