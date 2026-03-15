package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.EnumMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.DoorState;
import model.GameState;
import model.Player;
import model.ShortQuestion;
import model.TriviaDoor;
import model.TriviaMaze;
import model.TriviaRoom;

/**
 * Tests for GameState.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class GameStateTest {

    /***/
    private Player myPlayer;
    /***/
    private TriviaMaze myMaze;
    /***/
    private GameState myState;

    private GameStateTest() {
        setUp();
    }

    /** Builds a minimal 2x2 maze without needing a database. */
    private TriviaMaze buildMaze(final Player thePlayer) {
        final TriviaRoom[][] rooms = new TriviaRoom[2][2];
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                rooms[r][c] = new TriviaRoom(new EnumMap<>(Direction.class));
            }
        }
        final TriviaDoor door
            = new TriviaDoor(new ShortQuestion("Q?", 1, "A"), DoorState.CLOSED);
        rooms[0][0].addDoor(Direction.EAST, door);
        rooms[0][1].addDoor(Direction.WEST, door);
        return new TriviaMaze(rooms, thePlayer, 1, 1);
    }

    @BeforeEach
    void setUp() {
        myPlayer = new Player(0, 0);
        myMaze = buildMaze(myPlayer);
        myState = new GameState(myPlayer, myMaze);
    }

    // Constructor / getters

    @Test
    void testGetPlayerReturnsCorrectPlayer() {
        assertEquals(myPlayer, myState.getPlayer());
    }

    @Test
    void testGetMazeReturnsCorrectMaze() {
        assertEquals(myMaze, myState.getMaze());
    }

    @Test
    void testConstructWithNullPlayerAllowed() {
        final GameState state = new GameState(null, myMaze);
        assertNull(state.getPlayer());
    }

    @Test
    void testConstructWithNullMazeAllowed() {
        final GameState state = new GameState(myPlayer, null);
        assertNull(state.getMaze());
    }

    @Test
    void testConstructWithBothNullAllowed() {
        final GameState state = new GameState(null, null);
        assertNull(state.getPlayer());
        assertNull(state.getMaze());
    }

    // Player position is preserved

    @Test
    void testPlayerPositionPreserved() {
        final Player p = new Player(2, 3);
        final GameState state = new GameState(p, myMaze);
        assertEquals(2, state.getPlayer().getX());
        assertEquals(3, state.getPlayer().getY());
    }

    // Maze is same reference (no defensive copy in GameState)

    @Test
    void testMazeIsSameReference() {
        assertSame(myMaze, myState.getMaze());
    }

    @Test
    void testPlayerIsSameReference() {
        assertSame(myPlayer, myState.getPlayer());
    }

    // Different states are independent

    @Test
    void testTwoStatesHoldDifferentPlayers() {
        final Player otherPlayer = new Player(1, 1);
        final GameState otherState = new GameState(otherPlayer, myMaze);
        assertNotEquals(myState.getPlayer(), otherState.getPlayer());
    }

    @Test
    void testTwoStatesHoldDifferentMazes() {
        final Player otherPlayer = new Player(0, 0);
        final TriviaMaze otherMaze = buildMaze(otherPlayer);
        final GameState otherState = new GameState(otherPlayer, otherMaze);
        assertNotSame(myState.getMaze(), otherState.getMaze());
    }
}