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

        snakeGame = new SnakeGame(10, 10, 1, 3);
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

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        AABB bounds = snakeGame.getBounds();
        int widthInTiles = (int)bounds.getWidth();
        int heightInTiles = (int)bounds.getHeight();

        int tileWidth = (int)((float)contentWidth/widthInTiles);
        int tileHeight = (int)((float)contentHeight/heightInTiles);

        int tileSize = Math.min(tileWidth, tileHeight);

        //Color the board
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardPaint);

        //Paint the snake
        for (Vector3 position : snakeGame.getSnake().getPositions()){
            //TODO we should also divide by the size of the grid
            Vector3 normalized = position.sub(bounds.getMin());

            Vector3 mapped = normalized
                    .mul(tileSize);

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
            .mul(tileSize);

        canvas.drawRect(foodPos.getX(), foodPos.getY(), foodPos.getX()+ tileSize, foodPos.getY() + tileSize, foodPaint);
    }

    @Override
    public void render(SnakeGame game) {
        invalidate();
    }
}
