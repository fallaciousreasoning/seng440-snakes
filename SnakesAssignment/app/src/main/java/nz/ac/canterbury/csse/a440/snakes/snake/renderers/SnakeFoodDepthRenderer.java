package nz.ac.canterbury.csse.a440.snakes.snake.renderers;

import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;

/**
 * Renders the food depth of the snake game to a text view
 */
public class SnakeFoodDepthRenderer extends SnakeTextRenderer {
    /**
     * The string we format with the food depth
     */
    private String formatString = "Food Depth: %s";

    public SnakeFoodDepthRenderer() {
        super.setTextGetter(new GetTextFromSnakeGame() {
            @Override
            public String getText(SnakeGame game) {
                int depth = (game == null || game.getFood() == null) ? 0 : (int)game.getFood().getPosition().getZ();
                return String.format(formatString, depth);
            }
        });
    }

    /**
     * Sets the format string we use to display the depth
     *
     * @param formatString The format string
     */
    public void setFormatString(String formatString) {
        int first = formatString.indexOf("%s");
        int last = formatString.indexOf("%s");

        if (first == -1 || first != last)
            throw new RuntimeException("You should have exactly 1 format operator");

        this.formatString = formatString;
    }
}
