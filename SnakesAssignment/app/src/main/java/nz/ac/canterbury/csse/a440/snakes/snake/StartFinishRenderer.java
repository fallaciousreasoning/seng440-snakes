package nz.ac.canterbury.csse.a440.snakes.snake;

import android.widget.TextView;

/**
 * Handles updating the text telling the player to start or restart the game
 */
public class StartFinishRenderer implements Renderer {
    private TextView textView;
    private String finishText;
    private String startText;

    /**
     * Creates a new start finish renderer
     *
     * @param startText  The start text
     * @param finishText The finish text
     */
    public StartFinishRenderer(String startText, String finishText) {
        this.finishText = finishText;
        this.startText = startText;
    }

    /**
     * Sets the text view this uses to display the start and finish text
     *
     * @param textView
     */
    public void setTextView(TextView textView) {
        this.textView = textView;

        textView.setText(startText);
    }

    @Override
    public void render(SnakeGame game) {
        if (textView == null) return;

        if (!game.started()) textView.setText(startText);
        else if (game.finished()) textView.setText(finishText);
        else textView.setText("");
    }
}
