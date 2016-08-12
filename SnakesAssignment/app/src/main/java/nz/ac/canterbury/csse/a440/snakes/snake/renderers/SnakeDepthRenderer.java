package nz.ac.canterbury.csse.a440.snakes.snake.renderers;

import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;

/**
 * Renders the depth of the snake to a text view
 */
public class SnakeDepthRenderer extends SnakeTextRenderer {
    /**
     * The string we format with the depth
     */
    private String formatString = "Depth: %s";

    public SnakeDepthRenderer() {
        super.setTextGetter(new GetTextFromSnakeGame() {
            @Override
            public String getText(SnakeGame game) {
                int depth = (game == null || game.getSnake() == null) ? 0 : (int)game.getSnake().headPosition().getZ();
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
