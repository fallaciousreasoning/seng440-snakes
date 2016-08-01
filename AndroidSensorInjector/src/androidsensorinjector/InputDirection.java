package androidsensorinjector;

/**
 * A class for holding information about what a key is registered to
 */
public class InputDirection {
    private final Direction direction;
    private final InputMethod method;


    public InputDirection(InputMethod method, Direction direction) {
        this.direction = direction;
        this.method = method;
    }

    public Direction getDirection() {
        return direction;
    }

    public InputMethod getMethod() {
        return method;
    }
}
