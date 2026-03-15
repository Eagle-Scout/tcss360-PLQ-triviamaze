package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.Player;

/**
 * Tests for the Player class.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class PlayerTest {

    /***/
    private Player myPlayer;

    private PlayerTest() {
        setUp();
    }

    @BeforeEach
    private void setUp() {
        myPlayer = new Player(0, 0);
    }

    @Test
    void testInitialX() {
        assertEquals(0, myPlayer.getX());
    }

    @Test
    void testInitialY() {
        assertEquals(0, myPlayer.getY());
    }

    @Test
    void testNonZeroInitialPosition() {
        final Player player = new Player(3, 5);
        assertEquals(3, player.getX());
        assertEquals(5, player.getY());
    }

    @Test
    void testMoveNorthDecreasesY() {
        myPlayer.move(Direction.NORTH);
        assertEquals(-1, myPlayer.getY());
    }

    @Test
    void testMoveSouthIncreasesY() {
        myPlayer.move(Direction.SOUTH);
        assertEquals(1, myPlayer.getY());
    }

    @Test
    void testMoveEastIncreasesX() {
        myPlayer.move(Direction.EAST);
        assertEquals(1, myPlayer.getX());
    }

    @Test
    void testMoveWestDecreasesX() {
        myPlayer.move(Direction.WEST);
        assertEquals(-1, myPlayer.getX());
    }

    @Test
    void testMoveDoesNotChangeOtherAxisNS() {
        myPlayer.move(Direction.NORTH);
        assertEquals(0, myPlayer.getX());
    }

    @Test
    void testMoveDoesNotChangeOtherAxisEW() {
        myPlayer.move(Direction.EAST);
        assertEquals(0, myPlayer.getY());
    }

    @Test
    void testMultipleMoves() {
        myPlayer.move(Direction.EAST);
        myPlayer.move(Direction.EAST);
        myPlayer.move(Direction.SOUTH);
        assertEquals(2, myPlayer.getX());
        assertEquals(1, myPlayer.getY());
    }

    @Test
    void testMoveAndReturnToOrigin() {
        myPlayer.move(Direction.NORTH);
        myPlayer.move(Direction.SOUTH);
        assertEquals(0, myPlayer.getY());
    }
}