package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.DoorState;
import model.ShortQuestion;
import model.TriviaDoor;
import model.TriviaRoom;

/**
 * Tests for the TriviaRoom class.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class TriviaRoomTest {

    /***/
    private TriviaRoom myRoom;
    /***/
    private TriviaDoor myNorthDoor;
    /***/
    private TriviaDoor myEastDoor;

    private TriviaRoomTest() {
        setUp();
    }

    @BeforeEach
    private void setUp() {
        myRoom = new TriviaRoom(null);
        myNorthDoor = new TriviaDoor(new ShortQuestion("Q1", 1, "A1"), DoorState.CLOSED);
        myEastDoor = new TriviaDoor(new ShortQuestion("Q2", 2, "A2"), DoorState.CLOSED);
    }

    // Constructor

    @Test
    void testConstructWithNullDoesNotThrow() {
        assertDoesNotThrow(() -> new TriviaRoom(null));
    }

    @Test
    void testConstructWithEmptyMap() {
        final TriviaRoom room = new TriviaRoom(new EnumMap<>(Direction.class));
        assertFalse(room.hasDoor(Direction.NORTH));
    }

    @Test
    void testConstructWithPreloadedDoors() {
        final Map<Direction, TriviaDoor> doors = new EnumMap<>(Direction.class);
        doors.put(Direction.NORTH, myNorthDoor);
        final TriviaRoom room = new TriviaRoom(doors);
        assertTrue(room.hasDoor(Direction.NORTH));
    }

    // addDoor / hasDoor

    @Test
    void testAddDoorNorth() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        assertTrue(myRoom.hasDoor(Direction.NORTH));
    }

    @Test
    void testHasDoorFalseWhenNotAdded() {
        assertFalse(myRoom.hasDoor(Direction.SOUTH));
    }

    @Test
    void testAddMultipleDoors() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        myRoom.addDoor(Direction.EAST, myEastDoor);
        assertTrue(myRoom.hasDoor(Direction.NORTH));
        assertTrue(myRoom.hasDoor(Direction.EAST));
    }

    @Test
    void testAddDoorDoesNotAddOtherDirections() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        assertFalse(myRoom.hasDoor(Direction.SOUTH));
        assertFalse(myRoom.hasDoor(Direction.EAST));
        assertFalse(myRoom.hasDoor(Direction.WEST));
    }

    // getDoor()

    @Test
    void testGetDoorReturnsCorrectDoor() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        assertEquals(myNorthDoor, myRoom.getDoor(Direction.NORTH));
    }

    @Test
    void testGetDoorReturnsNullWhenAbsent() {
        assertNull(myRoom.getDoor(Direction.SOUTH));
    }

    // getQuestion()

    @Test
    void testGetQuestionFromDoor() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        assertEquals("Q1", myRoom.getQuestion(Direction.NORTH).getQuestion());
    }

    // getDoors()

    @Test
    void testGetDoorsIsUnmodifiable() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        final Map<Direction, TriviaDoor> doors = myRoom.getDoors();
        assertThrows(UnsupportedOperationException.class,
                () -> doors.put(Direction.SOUTH, myEastDoor));
    }

    @Test
    void testGetDoorsReflectsAddedDoors() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        myRoom.addDoor(Direction.EAST, myEastDoor);
        assertEquals(2, myRoom.getDoors().size());
    }

    @Test
    void testOverwriteDoor() {
        myRoom.addDoor(Direction.NORTH, myNorthDoor);
        final TriviaDoor replaceDoor
            = new TriviaDoor(new ShortQuestion("New Q", 3, "New A"), DoorState.OPEN);
        myRoom.addDoor(Direction.NORTH, replaceDoor);
        assertEquals(replaceDoor, myRoom.getDoor(Direction.NORTH));
    }
}