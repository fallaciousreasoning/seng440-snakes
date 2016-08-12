package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * An interface that indicates that something can be drawn with opengl
 */
public interface GLDrawable {
    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     *                  this shape.
     */
    void draw(float[] mvpMatrix);
}
