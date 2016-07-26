package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * Created by jayha on 26/07/2016.
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
     * Moves the game forward by one frame and tells the renderer to redraw
     */
    public void step() {
        if (getRenderer() != null) {
            getRenderer().render(this);
        }
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
}
