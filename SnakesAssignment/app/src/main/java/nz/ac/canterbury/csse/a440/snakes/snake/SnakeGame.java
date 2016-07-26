package nz.ac.canterbury.csse.a440.snakes.snake;

import java.util.Random;

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
     * The size of a tile
     */
    private float tileSize = 1;

    /**
     * Indicates the snake hit a wall
     */
    private boolean hitWall;

    /**
     * Indicates the snake hit itself
     */
    private boolean hitSelf;

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

        snake = new Snake(bounds.getCentre(), Direction.NORTH, 1, startingLength);
        spawnFood();
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

            //Check if we're close enough to the food to eat it
            if (Vector3.distance(snake.headPosition(), food.getPosition()) < tileSize){
                snake.grow(food.getGrowth());
                spawnFood();
            }
        }

        if (getRenderer() != null) {
            getRenderer().render(this);
        }
    }

    /**
     * Adds a new piece of food at a random position on the board
     */
    private void spawnFood() {
        float x = random.nextFloat() * bounds.getWidth() + bounds.getMin().getX();
        float y = random.nextFloat() * bounds.getHeight() + bounds.getMin().getY();
        float z = random.nextFloat() * bounds.getDepth() + bounds.getMin().getZ();

        food = new Food(1, new Vector3(x, y, z));
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
}
