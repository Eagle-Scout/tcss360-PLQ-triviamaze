package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.DoorState;

/**
 * Tests for the DoorState enum.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class DoorStateTest {

    private DoorStateTest() {

    }

    @Test
    void testOpenLabel() {
        assertEquals("Open", DoorState.OPEN.getDoorState());
    }

    @Test
    void testClosedLabel() {
        assertEquals("Closed", DoorState.CLOSED.getDoorState());
    }

    @Test
    void testLockedLabel() {
        assertEquals("Locked", DoorState.LOCKED.getDoorState());
    }

    @Test
    void testThreeStates() {
        assertEquals(3, DoorState.values().length);
    }
}