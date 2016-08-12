package nz.ac.canterbury.csse.a440.snakes.snake;

import android.widget.TextView;

/**
 * Renders the depth of the snake to a text view
 */
public class SnakeDepthRenderer implements Renderer {
    /**
     * The text view we render with
     */
    private TextView textView;

    /**
     * The string we format with the depth
     */
    private String formatString = "Depth: %s";

    /**
     * Sets the textview that this depth renderer draws to
     *
     * @param textView The text view to use
     */
    public void setTextView(TextView textView) {
        this.textView = textView;
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

    @Override
    public void render(SnakeGame game) {
        //If we don't have a text view we have nothing to
        if (textView == null) return;

        //Set the text
        textView.setText(String.format(formatString, game.getSnake().headPosition() + ""));
    }
}
