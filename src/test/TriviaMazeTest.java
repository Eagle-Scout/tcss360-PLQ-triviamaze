package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.DoorState;
import model.Player;
import model.ShortQuestion;
import model.TriviaDoor;
import model.TriviaMaze;
import model.TriviaRoom;

/**
 * Tests for TriviaMaze. Builds mazes manually to avoid needing a database.
 *
 * Layout used in most tests (2x2): (0,0) --E-- (1,0) | | S S | | (0,1) --E-- (1,1) <- EXIT
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class TriviaMazeTest {

    /***/
    private TriviaMaze myMaze;
    /***/
    private TriviaRoom[][] myRooms;

    private TriviaMazeTest() {
        setUp();
    }

    /** Convenience: builds a ShortQuestion with a known answer. */
    private ShortQuestion q(final String theAnswer) {
        return new ShortQuestion("Q?", 1, theAnswer);
    }

    /** Convenience: builds a CLOSED door. */
    private TriviaDoor closedDoor() {
        return new TriviaDoor(q("x"), DoorState.CLOSED);
    }

    /**
     * Builds a fully connected 2x2 maze: [0][0] <-> [0][1] (EAST/WEST) [0][0] <-> [1][0]
     * (SOUTH/NORTH) [0][1] <-> [1][1] (SOUTH/NORTH) [1][0] <-> [1][1] (EAST/WEST) Exit is at
     * (1,1) = row 1, col 1.
     */
    @BeforeEach
    private void setUp() {
        myRooms = new TriviaRoom[2][2];
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                myRooms[r][c] = new TriviaRoom(new EnumMap<>(Direction.class));
            }
        }

        // Shared doors (same object on both sides, matching real MazeGenerator behaviour)
        final TriviaDoor eastWest = closedDoor(); // (0,0)-(0,1)
        final TriviaDoor southNorth = closedDoor(); // (0,0)-(1,0)
        final TriviaDoor southNorth2 = closedDoor(); // (0,1)-(1,1)
        final TriviaDoor eastWest2 = closedDoor(); // (1,0)-(1,1)

        myRooms[0][0].addDoor(Direction.EAST, eastWest);
        myRooms[0][1].addDoor(Direction.WEST, eastWest);

        myRooms[0][0].addDoor(Direction.SOUTH, southNorth);
        myRooms[1][0].addDoor(Direction.NORTH, southNorth);

        myRooms[0][1].addDoor(Direction.SOUTH, southNorth2);
        myRooms[1][1].addDoor(Direction.NORTH, southNorth2);

        myRooms[1][0].addDoor(Direction.EAST, eastWest2);
        myRooms[1][1].addDoor(Direction.WEST, eastWest2);

        final Player player = new Player(0, 0);
        myMaze = new TriviaMaze(myRooms, player, 1, 1);
    }

    // ==================== getRoom ====================

    @Test
    void testGetRoomValidPosition() {
        assertNotNull(myMaze.getRoom(0, 0));
    }

    @Test
    void testGetRoomOutOfBoundsNegative() {
        assertNull(myMaze.getRoom(-1, 0));
    }

    @Test
    void testGetRoomOutOfBoundsPositive() {
        assertNull(myMaze.getRoom(5, 5));
    }

    @Test
    void testGetRoomCorrectRoom() {
        assertEquals(myRooms[0][0], myMaze.getRoom(0, 0));
    }

    // ==================== getCurrentRoom ====================

    @Test
    void testGetCurrentRoomAtStart() {
        assertEquals(myRooms[0][0], myMaze.getCurrentRoom());
    }

    // ==================== isExit ====================

    @Test
    void testIsExitFalseAtStart() {
        assertFalse(myMaze.isExit());
    }

    @Test
    void testIsExitTrueAtExit() {
        final Player exitPlayer = new Player(1, 1);
        final TriviaMaze maze = new TriviaMaze(myRooms, exitPlayer, 1, 1);
        assertTrue(maze.isExit());
    }

    // ==================== attemptMove ====================

    @Test
    void testAttemptMoveCorrectOpensDoorsAndMovesPlayer() {
        myMaze.attemptMove(Direction.EAST, true);
        assertEquals(1, myMaze.getPlayer().getX());
        assertEquals(0, myMaze.getPlayer().getY());
    }

    @Test
    void testAttemptMoveDoorOpensOnCorrect() {
        myMaze.attemptMove(Direction.EAST, true);
        assertEquals(DoorState.OPEN, myMaze.getRoom(0, 0).getDoor(Direction.EAST).getState());
    }

    @Test
    void testAttemptMoveWrongLocksDoorsNoMove() {
        myMaze.attemptMove(Direction.EAST, false);
        assertEquals(0, myMaze.getPlayer().getX());
        assertEquals(DoorState.LOCKED,
                myMaze.getRoom(0, 0).getDoor(Direction.EAST).getState());
    }

    @Test
    void testAttemptMoveNoDoorInDirectionDoesNothing() {
        // (0,0) has no NORTH door — nothing should happen
        myMaze.attemptMove(Direction.NORTH, true);
        assertEquals(0, myMaze.getPlayer().getX());
        assertEquals(0, myMaze.getPlayer().getY());
    }

    @Test
    void testAttemptMoveThroughOpenDoorMovesPlayer() {
        myMaze.getRoom(0, 0).getDoor(Direction.EAST).setState(DoorState.OPEN);
        myMaze.attemptMove(Direction.EAST, true);
        assertEquals(1, myMaze.getPlayer().getX());
    }

    // ==================== canLeaveMaze ====================

    @Test
    void testCanLeaveMazeTrueWhenAllOpen() {
        assertTrue(myMaze.canLeaveMaze());
    }

    @Test
    void testCanLeaveMazeFalseWhenAllPathsLocked() {
        // Lock every door reachable from (0,0)
        myRooms[0][0].getDoor(Direction.EAST).setState(DoorState.LOCKED);
        myRooms[0][0].getDoor(Direction.SOUTH).setState(DoorState.LOCKED);
        assertFalse(myMaze.canLeaveMaze());
    }

    @Test
    void testCanLeaveMazeTrueWithOnePathRemaining() {
        // Lock EAST from start, but SOUTH path to exit still works
        myRooms[0][0].getDoor(Direction.EAST).setState(DoorState.LOCKED);
        assertTrue(myMaze.canLeaveMaze());
    }

    @Test
    void testCanLeaveMazeTrueWhenAlreadyAtExit() {
        final Player exitPlayer = new Player(1, 1);
        final TriviaMaze maze = new TriviaMaze(myRooms, exitPlayer, 1, 1);
        assertTrue(maze.canLeaveMaze());
    }

    @Test
    void testCanLeaveMazeFalseWhenMiddlePathsLocked() {
        // Lock all doors leading toward (1,1)
        myRooms[0][0].getDoor(Direction.EAST).setState(DoorState.LOCKED);
        myRooms[0][0].getDoor(Direction.SOUTH).setState(DoorState.LOCKED);
        myRooms[0][1].getDoor(Direction.SOUTH).setState(DoorState.LOCKED);
        myRooms[1][0].getDoor(Direction.EAST).setState(DoorState.LOCKED);
        assertFalse(myMaze.canLeaveMaze());
    }

    // ==================== getRooms ====================

    @Test
    void testGetRoomsReturnsCopy() {
        final TriviaRoom[][] copy = myMaze.getRooms();
        copy[0][0] = null;
        assertNotNull(myMaze.getRoom(0, 0));
    }

    @Test
    void testGetRoomsCorrectDimensions() {
        assertEquals(2, myMaze.getRooms().length);
        assertEquals(2, myMaze.getRooms()[0].length);
    }
}