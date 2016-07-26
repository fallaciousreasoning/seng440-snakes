package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * A Game of Snake
 */
public class SnakeGame {
    /**
     * The active renderer for the game
     */
    private Renderable renderer;

    /**
     * The snake
     */
    private Snake snake;

    /**
     * The bounds the game should be played within
     */
    private AABB bounds;

    /**
     * Indicates the snake hit a wall
     */
    private boolean hitWall;

    /**
     * Indicates the snake hit itself
     */
    private boolean hitSelf;

    public SnakeGame(AABB bounds, int startingLength) {
        this.bounds = bounds;

        snake = new Snake(bounds.getCentre(), Direction.NORTH, 1, startingLength);
    }

    /**
     * Moves the game forward by one frame and tells the renderer to redraw
     */
    public void step() {
        //If the snake is off bounds, set the finished flag
        if (!bounds.contains(snake.nextPosition())){
            hitWall = true;
        }
        //If the snake will intersect itself, set the finished flag
        else if (snake.willIntersect()) {
            hitSelf = true;
        }
        //If we haven't died, step!
        else {
            snake.step();
        }

        if (getRenderer() != null) {
            getRenderer().render(this);
        }
    }

    /**
     * Sets the direction the snake will be heading
     * @param direction The direction for the snake to go
     */
    public void setDirection(Direction direction) {
        snake.setDirection(direction);
    }

    /**
     * Gets the current renderer for the game
     * @return gets the current game renderer
     */
    public Renderable getRenderer() {
        return renderer;
    }

    /**
     * Sets the current renderer for the game
     * @param renderer The new renderer for the game
     */
    public void setRenderer(Renderable renderer) {
        this.renderer = renderer;
    }

    /**
     * Gets the snake
     * @return The current snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Indicates the snake has hit a wall
     * @return Whether the snake has hit the wall
     */
    public boolean hitWall() {
        return hitWall;
    }

    /**
     * Indicates the snake has hit itself
     * @return Whether the snake has hit itself
     */
    public boolean hitSelf() {
        return hitSelf;
    }

    /**
     * Indicates the game is over
     * @return Whether the game has been finished
     */
    public boolean finished() {
        return hitSelf || hitWall;
    }
}
