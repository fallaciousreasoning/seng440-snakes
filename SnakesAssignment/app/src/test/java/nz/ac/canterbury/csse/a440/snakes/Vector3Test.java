package nz.ac.canterbury.csse.a440.snakes;

import org.junit.Test;

import static org.junit.Assert.*;

import nz.ac.canterbury.csse.a440.snakes.snake.Vector3;

/**
 * Created by jayha on 26/07/2016.
 */
public class Vector3Test {
    private Vector3 first = new Vector3(0.5f, 1, 2);
    private Vector3 second = new Vector3(1, 0.5f, 2);

    @Test
    public void testAdd(){
        assertEquals(new Vector3(1.5f, 1.5f, 4), first.add(second));
    }

    @Test
    public void testSub() {
        assertEquals(new Vector3(-0.5f, 0.5f, 0), first.sub(second));
    }

    @Test
    public void testMulFloat() {
        assertEquals(new Vector3(1, 2,  4), first.mul(2));
    }

    @Test
    public void testMul() {
        assertEquals(new Vector3(0.5f, 0.5f, 4), first.mul(second));
    }

    @Test
    public void testDivFloat() {
        assertEquals(new Vector3(0.25f, 0.5f, 1), first.div(2));
    }

    @Test
    public void testDiv() {
        assertEquals(new Vector3(0.5f, 2, 1), first.div(second));
    }

    @Test
    public void testLength() {
        assertEquals(Math.sqrt(0.5*0.5 + 1 + 4), first.length(), 0.1);
    }

    @Test
    public void testNormalize() {
        Vector3 almostUnit = new Vector3(0.5f, 0, 0);
        Vector3 unit = almostUnit.normalized();

        assertEquals(new Vector3(1, 0, 0), unit);
    }
}
