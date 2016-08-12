package nz.ac.canterbury.csse.a440.snakes.snake;

/**
 * A simple class containing some hard coded gl colors
 */
public class GLColor {
    public static final float[] Black = {0, 0, 0, 1};
    public static final float[] White = {1, 1, 1, 1};
    public static final float[] Gray = {0.5f, 0.5f, 0.5f, 1};
    public static final float[] Red = {1, 0, 0, 1};
    public static final float[] Green = {0, 1, 0, 1};
    public static final float[] Blue = {0, 0, 1, 1};

    /**
     * A helper function for lerping colors
     *
     * @param a      The start color
     * @param b      The end color
     * @param amount The amount to lerp
     * @return The result color
     */
    public static float[] Lerp(float[] a, float[] b, float amount) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Colors must have the same length");

        float[] result = new float[a.length];
        for (int i = 0; i < result.length; ++i) {
            float dif = b[i] - a[i];
            float n = a[i] + dif * amount;
            result[i] = n;
        }

        return result;
    }
}
