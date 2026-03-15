package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.Direction;

/**
 * Tests for the Direction enum.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class DirectionTest {

    private DirectionTest() {

    }

    @Test
    void testNorthDX() {
        assertEquals(0, Direction.NORTH.getDX());
    }

    @Test
    void testNorthDY() {
        assertEquals(-1, Direction.NORTH.getDY());
    }

    @Test
    void testSouthDX() {
        assertEquals(0, Direction.SOUTH.getDX());
    }

    @Test
    void testSouthDY() {
        assertEquals(1, Direction.SOUTH.getDY());
    }

    @Test
    void testEastDX() {
        assertEquals(1, Direction.EAST.getDX());
    }

    @Test
    void testEastDY() {
        assertEquals(0, Direction.EAST.getDY());
    }

    @Test
    void testWestDX() {
        assertEquals(-1, Direction.WEST.getDX());
    }

    @Test
    void testWestDY() {
        assertEquals(0, Direction.WEST.getDY());
    }

    // opposite()

    @Test
    void testNorthOpposite() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite());
    }

    @Test
    void testSouthOpposite() {
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite());
    }

    @Test
    void testEastOpposite() {
        assertEquals(Direction.WEST, Direction.EAST.opposite());
    }

    @Test
    void testWestOpposite() {
        assertEquals(Direction.EAST, Direction.WEST.opposite());
    }

    @Test
    void testOppositeIsSymmetric() {
        for (final Direction dir : Direction.values()) {
            assertEquals(dir, dir.opposite().opposite());
        }
    }

    // values count

    @Test
    void testFourDirections() {
        assertEquals(4, Direction.values().length);
    }
}