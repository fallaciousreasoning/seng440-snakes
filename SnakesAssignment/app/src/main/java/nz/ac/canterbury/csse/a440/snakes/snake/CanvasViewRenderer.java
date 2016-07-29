package nz.ac.canterbury.csse.a440.snakes.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import nz.ac.canterbury.csse.a440.snakes.R;

/**
 * TODO: document your custom view class.
 */
public class CanvasViewRenderer extends View implements Renderable {
    private Paint snakePaint;
    private Paint boardPaint;
    private Paint foodPaint;

    /**
     * The height of the text
     */
    private float textHeight;

    /**
     * The width of the text;
     */
    private float textWidth;

    /**
     * The game that this view is rendering
     */
    private SnakeGame snakeGame;

    public CanvasViewRenderer(Context context) {
        super(context);
        init(null, 1);
    }

    public CanvasViewRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 1);
    }

    public CanvasViewRenderer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CanvasViewRenderer,
                0, 0);

        try {
            //TODO load colors like a real person!
        } finally {
            a.recycle();
        }

        snakeGame = new SnakeGame(20, 30, 1, 3);
        snakeGame.setRenderer(this);

        snakePaint = new Paint();
        snakePaint.setStyle(Paint.Style.FILL);
        snakePaint.setColor(Color.argb(255, 0, 0, 0));

        boardPaint = new Paint();
        boardPaint.setStyle(Paint.Style.FILL);
        boardPaint.setColor(Color.argb(255, 255, 255, 255));

        foodPaint = new Paint();
        foodPaint.setStyle(Paint.Style.FILL);
        foodPaint.setColor(Color.argb(255, 255, 0, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        Vector3 offset = new Vector3(paddingLeft, paddingTop);

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        AABB bounds = snakeGame.getBounds();
        float widthInTiles = bounds.getWidth();
        float heightInTiles = bounds.getHeight();

        float tileWidth = contentWidth/widthInTiles;
        float tileHeight = contentHeight/heightInTiles;

        float tileSize = Math.min(tileWidth, tileHeight);

        float boardWidth = tileSize * widthInTiles;
        float boardHeight = tileSize * heightInTiles;

        //Centre the board we're rendering
        offset = offset.add(new Vector3(contentWidth - boardWidth, contentHeight - boardHeight).mul(0.5f));

        //Color the board
        canvas.drawRect(offset.getX(), offset.getY(), offset.getX() + boardWidth, offset.getY() + boardHeight, boardPaint);

        //Paint the snake
        for (Vector3 position : snakeGame.getSnake().getPositions()){
            //TODO we should also divide by the size of the grid
            Vector3 normalized = position.sub(bounds.getMin());

            Vector3 mapped = normalized
                    .mul(tileSize)
                    .add(offset);

            canvas.drawRect(
                    mapped.getX(),
                    mapped.getY(),
                    mapped.getX() + tileSize,
                    mapped.getY() + tileSize,
                    snakePaint);
        }

        Vector3 foodPos = snakeGame.getFood().getPosition();
        foodPos = foodPos.sub(bounds.getMin())
            //TODO divide by the tile size
            .mul(tileSize)
            .add(offset);

        canvas.drawRect(foodPos.getX(), foodPos.getY(), foodPos.getX()+ tileSize, foodPos.getY() + tileSize, foodPaint);
    }

    @Override
    public void render(SnakeGame game) {
        invalidate();
    }

    /**
     * Gets the current game
     * @return The current game
     */
    public SnakeGame getGame(){
        return snakeGame;
    }

    /**
     * Sets the current game
     * @param game The new game
     */
    public void setGame(SnakeGame game) {
        this.snakeGame = game;
        game.setRenderer(this);
        invalidate();
    }
}
