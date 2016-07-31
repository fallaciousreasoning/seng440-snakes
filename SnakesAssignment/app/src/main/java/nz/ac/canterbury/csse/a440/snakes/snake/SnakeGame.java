package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * A Game of Snake
 */
public class SnakeGame {
    /**
     * The active renderer for the game
     */
    private Collection<Renderer> renderers = new ArrayList<>();

    /**
     * The controller that tells the snake how it should move
     */
    private SnakeController snakeController;

    /**
     * The snake
     */
    private Snake snake;

    /**
     * The bounds the game should be played within
     */
    private AABB bounds;

    /**
     * The size of a tile
     */
    private float tileSize = 1;

    /**
     * The starting length of the snake. Used when resetting.
     */
    private final int startingLength;

    /**
     * Indicates the snake hit a wall
     */
    private boolean hitWall;

    /**
     * Indicates the snake hit itself
     */
    private boolean hitSelf;

    /**
     * Indicates whether the snake game has started
     */
    private boolean started;

    /**
     * A random number generator
     */
    private static Random random = new Random();

    /**
     * The food the snake is currently trying to eat
     */
    private Food food;

    public SnakeGame(int width, int height, int depth, int startingLength) {
        this.bounds = new AABB(Vector3.Zero, width*tileSize, height*tileSize, depth*tileSize);
        this.startingLength = startingLength;

        reset();
    }

    /**
     * Initializes the snake game. The game will not run until this method has been called.
     */
    public void start() {
        started = true;
        notifyListeners();
    }

    /**
     * Moves the game forward by one frame and tells the renderer to redraw
     */
    public void step() {
        //If we've finished, we shouldn't be attempting to step
        if (finished() || !started()) {
            return;
        }

        //If we have a snake controller, we should tell the snake where to go
        if (getSnakeController() != null) {
            snake.setDirection(getSnakeController().getDirection());
        }

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

            //Check if we're close enough to the food to eat it
            if (Vector3.distance(snake.headPosition(), food.getPosition()) < tileSize){
                snake.grow(food.getGrowth());
                spawnFood();
            }
        }

        notifyListeners();
    }

    /**
     * Notifies listeners that the game has been updated
     */
    private void notifyListeners() {
        //Tell all the renderers we've updated
        for (Renderer renderer : renderers)
            renderer.render(this);
    }

    /**
     * Resets the game
     */
    public void reset() {
        snake = new Snake(bounds.getCentre(), Direction.NORTH, 1, startingLength);
        spawnFood();

        started = false;
        hitWall = false;
        hitSelf = false;

        if (snakeController != null) {
            snakeController.reset();
        }

        notifyListeners();
    }

    /**
     * Adds a new piece of food at a random position on the board
     */
    private void spawnFood() {
        Vector3 position;

        do {
            float x = (float) Math.floor(random.nextFloat() * bounds.getWidth() + bounds.getMin().getX() / tileSize) * tileSize;
            float y = (float) Math.floor(random.nextFloat() * bounds.getHeight() + bounds.getMin().getY() / tileSize) * tileSize;
            float z = (float) Math.round(random.nextFloat() * bounds.getDepth() + bounds.getMin().getZ() / tileSize) * tileSize;
            position = new Vector3(x, y, z);
        }while (getSnake().onSnake(position));

        food = new Food(1, position);
    }

    /**
     * Gets the current renderers for the game
     * @return gets the current game renderers
     */
    public Collection<Renderer> getRenderers() {
        return renderers;
    }

    /**
     * Adds a renderer to the game
     * @param renderer The renderer to add to the game
     */
    public void addRenderer(Renderer renderer) {
        this.renderers.add(renderer);
    }

    /**
     * Gets the snake
     * @return The current snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Gets the starting length of the snake
     * @return The starting length of the snake
     */
    public int startingLength() {
        return startingLength;
    }

    /**
     * Gets the bounds of the game
     * @return The bounds
     */
    public AABB getBounds() {
        return bounds;
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

    /**
     * Indicates whether the game has been started
     * @return Whether the game has been started
     */
    public boolean started() {
        return started;
    }

    /**
     * Calculates the current score for the game
     * @return The score
     */
    public int score() {
        //The score is the length of the snake minus however long we were when we started
        return getSnake().length() - startingLength();
    }

    /**
     * Gets the size of a tile on the board
     * @return The size of a tile on the board
     */
    public float getTileSize() {
        return tileSize;
    }

    /**
     * The food the snake is trying to eat
     * @return The food the snake is trying to eat
     */
    public Food getFood() {
        return food;
    }

    /**
     * Gets the current controller for the snake
     * @return The controller for the snake
     */
    public SnakeController getSnakeController() {
        return snakeController;
    }

    /**
     * Sets the controller for the snake
     * @param snakeController The controller for the snake
     */
    public void setSnakeController(SnakeController snakeController) {
        this.snakeController = snakeController;
    }
}
