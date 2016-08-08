package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;

/**
 * Created by wooll on 07-Aug-16.
 */
public class Util {

    public static void writeGame(Context ctx, SnakeGame game) {
        try {
            ObjectOutput out = new ObjectOutputStream(ctx.openFileOutput("snakes.txt", Context.MODE_PRIVATE));
            out.writeObject(game);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SnakeGame readGame(Context ctx) {
        try {
            ObjectInputStream input = new ObjectInputStream(ctx.openFileInput("snakes.txt"));
            SnakeGame game = (SnakeGame) input.readObject();
            input.close();
            return game;
        } catch (FileNotFoundException e) {
            // Game never saved
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
