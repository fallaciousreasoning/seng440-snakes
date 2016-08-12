package nz.ac.canterbury.csse.a440.snakes.snake;

import android.widget.TextView;

/**
 * Renders some text relating to the snake game to a text view
 */
public class SnakeTextRenderer implements Renderer {
    /**
     * The text view we render with
     */
    private TextView textView;

    /**
     * The getter for the text
     */
    private GetTextFromSnakeGame textGetter;

    /**
     * Sets the textview that this text renderer draws to
     *
     * @param textView The text view to use
     */
    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void render(SnakeGame game) {
        //If we don't have a text view we have nothing to
        if (textView == null) return;
        if (textGetter == null) return;

        //Set the text
        textView.setText(textGetter.getText(game));
    }

    /**
     * Sets the text getter for this renderer
     * @param textGetter The text getter
     */
    public void setTextGetter(GetTextFromSnakeGame textGetter) {
        this.textGetter = textGetter;
    }
}

interface GetTextFromSnakeGame {
    String getText(SnakeGame game);
}
