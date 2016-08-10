package nz.ac.canterbury.csse.a440.snakes;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;

/**
 * Created by wooll on 07-Aug-16.
 */
public class Util {

    private static final Gson gson = new Gson();

    public static void writeGame(Context ctx, SnakeGame game) {
        try {
            FileOutputStream out = ctx.openFileOutput("snakesOnAPlane", Context.MODE_PRIVATE);
            String json = gson.toJson(game);
            out.write(json.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SnakeGame readGame(Context ctx) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(ctx.openFileInput("snakesOnAPlane")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            SnakeGame game = (SnakeGame) gson.fromJson(sb.toString(), SnakeGame.class);
            input.close();
            return game;
        } catch (FileNotFoundException e) {
            // Game never saved
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
