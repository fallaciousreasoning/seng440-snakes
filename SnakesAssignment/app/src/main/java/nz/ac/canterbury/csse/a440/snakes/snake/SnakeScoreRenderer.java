package nz.ac.canterbury.csse.a440.snakes.snake;

import android.widget.TextView;

/**
 * Renders the score of a snake game to a text view
 */
public class SnakeScoreRenderer extends SnakeTextRenderer {
    /**
     * The string we format with the score
     */
    private String formatString = "Score: %s";

    public SnakeScoreRenderer() {
        super.setTextGetter(new GetTextFromSnakeGame() {
            @Override
            public String getText(SnakeGame game) {
                int score = (game == null || game.getSnake() == null) ? 0 : game.score();
                return String.format(formatString, score);
            }
        });
    }

    /**
     * Sets the format string we use to display the score
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
