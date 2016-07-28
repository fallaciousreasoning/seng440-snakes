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
    public void testSetupAABB() {
        AABB aabb = new AABB(Vector3.One.mul(-1), Vector3.One);
        assertEquals(2, aabb.getWidth(), 0.001);
        assertEquals(2, aabb.getHeight(), 0.001);
        assertEquals(2, aabb.getDepth(), 0.001);

        assertEquals(Vector3.Zero, aabb.getCentre());

        aabb = new AABB(Vector3.Zero, 1, 1, 1);
        assertEquals(1, aabb.getWidth(), 0.001);
        assertEquals(1, aabb.getHeight(), 0.001);
        assertEquals(1, aabb.getDepth(), 0.001);

        assertEquals(Vector3.Zero, aabb.getCentre());
    }

    @Test
    public void testContains() {
        AABB aabb = new AABB(Vector3.One.mul(100), 10, 10, 10);

        assertTrue(aabb.contains(Vector3.One.mul(100)));
        assertTrue(aabb.contains(Vector3.One.mul(95)));
        assertTrue(aabb.contains(Vector3.One.mul(104)));

        assertFalse(aabb.contains(Vector3.One.mul(105)));
        assertFalse(aabb.contains(Vector3.One.mul(94)));

        aabb = new AABB(Vector3.Zero, 0, 0, 0);
        //assertTrue(aabb.contains(Vector3.Zero));
    }

    @Test
    public void testIntersects() {
        AABB first = new AABB(Vector3.One, 2, 2, 2);
        AABB second = new AABB(Vector3.Zero, 1, 1, 1);

        assertTrue(first.intersects(second));

        second = new AABB(Vector3.One.mul(-3), 1, 1, 1);

        assertFalse(first.intersects(second));
    }
}
