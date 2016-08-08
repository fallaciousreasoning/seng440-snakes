package nz.ac.canterbury.csse.a440.snakes.snake;

import android.opengl.Matrix;

/**
 * A cube for drawing in opengl
 */
public class GLCube implements GLDrawable{
    private GLSquare square;
    private Vector3 centre;
    private Vector3 halfSize;

    public GLCube(Vector3 centre, float size, float[] color)
    {
        this.halfSize = new Vector3(size, size, size).mul(0.5f);
        this.centre = centre;
        square = new GLSquare(new Vector3(0, 0, halfSize.getZ()), size, color);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        //Top facing wall
        drawSquare(mvpMatrix, centre, new Vector3(0, 0, 1), 90);

        //West facing wall
        drawSquare(mvpMatrix, centre, new Vector3(1, 0, 0), 90);

        //East facing wall
        drawSquare(mvpMatrix, centre, new Vector3(1, 0, 0), - 90);

        //North facing wall
        drawSquare(mvpMatrix, centre, new Vector3(0, 1, 0), - 90);

        //South facing wall
        drawSquare(mvpMatrix, centre, new Vector3(0, 1, 0), 90);

        //Bottom facing wall
        drawSquare(mvpMatrix, centre, new Vector3(0, 1, 0), 180);;
        //drawSquare(mvpMatrix, centre.add(halfSize.sub(new Vector3(0, 0, halfSize.getZ())).mul(2)),new Vector3(0, 0, 1), 0);
    }

    private void drawSquare(float[] view, Vector3 position, Vector3 rotationAxis, float rotation) {
        float[] copy = copy(view);
        Matrix.translateM(copy, 0, position.getX(), position.getY(), position.getZ());
        Matrix.rotateM(copy, 0, rotation, rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
        square.draw(copy);
    }

    private float[] copy(float[] matrix) {
        float[] customMvp = new float[matrix.length];
        for (int i =0; i < matrix.length; ++i) customMvp[i] = matrix[i];
        return customMvp;
    }
}
