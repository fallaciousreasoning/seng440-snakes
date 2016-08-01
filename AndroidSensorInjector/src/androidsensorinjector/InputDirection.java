package androidsensorinjector;

/**
 * A class for holding information about what a key is registered to
 */
public class InputDirection {
    private final Direction direction;
    private final InputMethod method;


    /**
     * Creates a new input direction class
     * @param method The input method to inject
     * @param direction THe direction to inject
     */
    public InputDirection(InputMethod method, Direction direction) {
        this.direction = direction;
        this.method = method;
    }

    /**
     * Gets the direction
     * @return The direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Gets the input method
     * @return The input method
     */
    public InputMethod getMethod() {
        return method;
    }
}
