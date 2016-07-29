package nz.ac.canterbury.csse.a440.snakes.snake;

import java.io.PrintStream;

/**
 * Created by jayha on 26/07/2016.
 */
public class ASCIIRenderer implements Renderer {
    private PrintStream output;

    private String snakeChar= "*";
    private String columnSeparator = "|";
    private String rowSeparator = "\n";

    public ASCIIRenderer(){
        this(System.out);
    }

    public ASCIIRenderer(PrintStream output) {
        this.output = output;
    }

    @Override
    public void render(SnakeGame game) {
        AABB bounds = game.getBounds();

        int width = (int)bounds.getWidth();
        int height = (int)bounds.getHeight();

        char[][] board = new char[height][width];
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y){
                board[y][x] = ' ';
            }

        for (int y = 0; y < height; ++y) {
            StringBuilder row = new StringBuilder(width * 2);

            for (int x = 0; x < width; ++x) {
                if (x != 0) {
                    row.append(columnSeparator);
                }
                row.append(board[y][x]);
            }

            row.append(rowSeparator);
            output.println(row.toString());
        }
    }
}
