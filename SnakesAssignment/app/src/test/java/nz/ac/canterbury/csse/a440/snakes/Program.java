package nz.ac.canterbury.csse.a440.snakes;

import java.util.Scanner;

import nz.ac.canterbury.csse.a440.snakes.snake.ASCIIRenderer;
import nz.ac.canterbury.csse.a440.snakes.snake.Direction;
import nz.ac.canterbury.csse.a440.snakes.snake.SnakeGame;

/**
 * Created by jayha on 26/07/2016.
 */
public class Program {
    public static void main(String[] args){
        System.out.println("Hello World");

        SnakeGame game = new SnakeGame(10, 10, 1, 3);
        game.setRenderer(new ASCIIRenderer());

        Scanner scanner = new Scanner(System.in);

        while (!game.finished()){
            System.out.println("What way are you going (n/e/s/e)?");

            String directionString = scanner.next("n|e|s|w");
            Direction direction;
            switch (directionString){
                case "n":
                    direction = Direction.NORTH;
                    break;

                case "s":
                    direction = Direction.SOUTH;
                    break;

                case "e":
                    direction = Direction.EAST;
                    break;

                default:
                    direction = Direction.WEST;
                    break;
            }

            game.setDirection(direction);
            game.step();
        }

        System.out.println("Finished!");
    }
}
