package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * Created by wooll on 10-Aug-16.
 */
public class SnakeRendererState {
    private SnakeGame game;
    private Vector3 lastKnownTail;
    private Vector3 lastKnownFood;
    private Vector3 lastKnownHead;

    public SnakeGame getGame() {
        return game;
    }

    public void setGame(SnakeGame game) {
        this.game = game;
    }

    public Vector3 getLastKnownTail() {
        return lastKnownTail;
    }

    public void setLastKnownTail(Vector3 lastKnownTail) {
        this.lastKnownTail = lastKnownTail;
    }

    public Vector3 getLastKnownFood() {
        return lastKnownFood;
    }

    public void setLastKnownFood(Vector3 lastKnownFood) {
        this.lastKnownFood = lastKnownFood;
    }

    public Vector3 getLastKnownHead() {
        return lastKnownHead;
    }

    public void setLastKnownHead(Vector3 lastKnownHead) {
        this.lastKnownHead = lastKnownHead;
    }
}
