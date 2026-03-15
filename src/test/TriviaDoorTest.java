package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.DoorState;
import model.ShortQuestion;
import model.TriviaDoor;

/**
 * Tests for the TriviaDoor class.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class TriviaDoorTest {

    /***/
    private TriviaDoor myDoor;
    /***/
    private ShortQuestion myQuestion;

    private TriviaDoorTest() {
        setUp();
    }

    @BeforeEach
    private void setUp() {
        myQuestion = new ShortQuestion("What is 2+2?", 1, "4");
        myDoor = new TriviaDoor(myQuestion, DoorState.CLOSED);
    }

    @Test
    void testInitialStateClosed() {
        assertEquals(DoorState.CLOSED, myDoor.getState());
    }

    @Test
    void testInitialQuestion() {
        assertEquals(myQuestion, myDoor.getQuestion());
    }

    @Test
    void testConstructWithOpenState() {
        final TriviaDoor door = new TriviaDoor(myQuestion, DoorState.OPEN);
        assertEquals(DoorState.OPEN, door.getState());
    }

    @Test
    void testConstructWithLockedState() {
        final TriviaDoor door = new TriviaDoor(myQuestion, DoorState.LOCKED);
        assertEquals(DoorState.LOCKED, door.getState());
    }

    // setState()

    @Test
    void testSetStateToOpen() {
        myDoor.setState(DoorState.OPEN);
        assertEquals(DoorState.OPEN, myDoor.getState());
    }

    @Test
    void testSetStateToLocked() {
        myDoor.setState(DoorState.LOCKED);
        assertEquals(DoorState.LOCKED, myDoor.getState());
    }

    @Test
    void testSetStateBackToClosed() {
        myDoor.setState(DoorState.OPEN);
        myDoor.setState(DoorState.CLOSED);
        assertEquals(DoorState.CLOSED, myDoor.getState());
    }

    // setQuestion()

    @Test
    void testSetQuestion() {
        final ShortQuestion newQ = new ShortQuestion("What is 3+3?", 2, "6");
        myDoor.setQuestion(newQ);
        assertEquals(newQ, myDoor.getQuestion());
    }

    @Test
    void testSetQuestionDoesNotAffectState() {
        myDoor.setState(DoorState.OPEN);
        myDoor.setQuestion(new ShortQuestion("New question", 2, "answer"));
        assertEquals(DoorState.OPEN, myDoor.getState());
    }
}