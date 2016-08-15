package nz.ac.canterbury.csse.a440.snakes;

import org.junit.Test;

import nz.ac.canterbury.csse.a440.snakes.snake.AABB;
import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

import static org.junit.Assert.*;

/**
 * Created by jayha on 26/07/2016.
 */
public class TestAABB {
    @Test
    public void testSetupWidth1() {
        AABB aabb = new AABB(Vector3.One.mul(-1), Vector3.One);
        assertEquals(2, aabb.getWidth(), 0.001);
    }

    @Test
    public void testSetupHeight1() {
        AABB aabb = new AABB(Vector3.One.mul(-1), Vector3.One);
        assertEquals(2, aabb.getHeight(), 0.001);
    }

    @Test
    public void testSetupDepth1() {
        AABB aabb = new AABB(Vector3.One.mul(-1), Vector3.One);
        assertEquals(2, aabb.getDepth(), 0.001);
    }

    @Test
    public void testSetupCentre1() {
        AABB aabb = new AABB(Vector3.One.mul(-1), Vector3.One);
        assertEquals(Vector3.Zero, aabb.getCentre());
    }

    @Test
    public void testContains1() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertTrue(aabb.contains(Vector3.One.mul(100)));
    }

    @Test
    public void testContains2() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertTrue(aabb.contains(Vector3.One.mul(95)));
    }

    @Test
    public void testContains3() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertTrue(aabb.contains(Vector3.One.mul(104)));
    }

    @Test
    public void testContains4() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertFalse(aabb.contains(Vector3.One.mul(105)));;
    }

    @Test
    public void testContains5() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertFalse(aabb.contains(Vector3.One.mul(94)));
    }



    @Test
    public void testIntersects1() {
        AABB first = new AABB(Vector3.One, 2, 2, 2);
        AABB second = new AABB(Vector3.Zero, 1, 1, 1);

        assertTrue(first.intersects(second));
    }

    @Test
    public void testIntersects2() {
        AABB first = new AABB(Vector3.One, 2, 2, 2);
        AABB second = new AABB(Vector3.One.mul(-3), 1, 1, 1);

        assertFalse(first.intersects(second));
    }
}
