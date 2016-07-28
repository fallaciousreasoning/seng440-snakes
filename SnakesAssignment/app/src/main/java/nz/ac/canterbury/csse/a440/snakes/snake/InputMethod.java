package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * An enum of the different control methods. This is primarily used with
 * settings to make them more readable
 */
public enum InputMethod {
    SWIPE,
    BUTTONS,
    ACCELEROMETER,
    COMPASS;

    /**
     * Gets a InputMethod from a number
     * @param value The value
     * @return The input method
     */
    public static InputMethod fromInt(int value) {
        return values()[value];
    }
}
