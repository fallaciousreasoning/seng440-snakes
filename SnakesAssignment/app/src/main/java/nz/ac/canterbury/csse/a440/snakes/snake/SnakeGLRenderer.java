package nz.ac.canterbury.csse.a440.snakes.snake;

import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A renderer for the snake game implemented in opengl
 */
public class SnakeGLRenderer implements GLSurfaceView.Renderer, Renderer {
    private static final String TAG = "SnakeGLRenderer";

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final float[] backgroundColor = GLColor.Lerp(GLColor.Black, GLColor.White, 0.8f);
    private final float[] snakeColor = GLColor.Lerp(GLColor.Black, GLColor.White, 0.4f);
    private final float[] foodColor = GLColor.Red;
    private final float[] snakeHeadColor = GLColor.Black;

    private float aspectRatio;

    private SnakeGame snakeGame;
    private boolean render3D = true;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(
                backgroundColor[0],
                backgroundColor[1],
                backgroundColor[2],
                backgroundColor[3]
        );
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        List<GLDrawable> drawables = new ArrayList<>();

        //Don't draw anything if the game is null
        if (snakeGame == null) return;

        float tileWidth = 2*aspectRatio/(snakeGame.getBounds().getWidth());
        float tileHeight = 2/(snakeGame.getBounds().getHeight());
        float tileSize = Math.min(tileWidth, tileHeight);

        Vector3 foodPosition = toGLCoordinates(
                snakeGame
                .getFood()
                .getPosition(), tileSize);

        GLDrawable food = getDrawable(foodPosition, tileSize, foodColor);

        drawables.add(food);

        for (Vector3 position : snakeGame.getSnake().getPositions()) {
            Vector3 drawPosition = toGLCoordinates(position, tileSize);
            drawables.add(getDrawable(drawPosition, tileSize, snakeColor));
        }

        Vector3 position = toGLCoordinates(snakeGame.getSnake().headPosition(), tileSize);
        drawables.add(getDrawable(position, tileSize, snakeHeadColor));


        //Render everything
        for (GLDrawable drawable : drawables)
            drawable.draw(mMVPMatrix);
    }

    /**
     * Creates a new drawable
     * @param position The position
     * @param tileSize The tilesize
     * @param color The color
     * @return The drawable
     */
    private GLDrawable getDrawable(Vector3 position, float tileSize, float[] color) {
        return isRender3D() ?
                new GLCube(position, tileSize, color) :
                new GLSquare(position, tileSize, color);
    }

    /**
     * Gets the GL coordinates for a game position
     * @param position The position of the snake
     * @return The gl coordinates
     */
    private Vector3 toGLCoordinates(Vector3 position, float tileSize) {
        Vector3 halfSize = new Vector3(tileSize, tileSize, tileSize).mul(0.5f);
        return position
                .sub(new Vector3(0, 0, 0))
                .mul(tileSize)
                .add(halfSize);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        aspectRatio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, aspectRatio, -aspectRatio, 1, -1, render3D ? 2 : 3, 7);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    @Override
    public void render(SnakeGame game) {
        this.snakeGame = game;
    }

    /**
     * Indicates whether the game should be rendered in 3D
     * @return Whether the game should render in 3D
     */
    public boolean isRender3D() {
        return render3D;
    }

    /**
     * Sets whether the game is rendered in 3D
     * @param render3D Whether the game should be rendered in 3D
     */
    public void setRender3D(boolean render3D) {
        this.render3D = render3D;
    }
}
