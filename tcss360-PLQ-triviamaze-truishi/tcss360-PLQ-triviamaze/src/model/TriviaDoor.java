package model;

import java.io.Serializable;

/**
 * TriviaDoor
 *
 * Represents a door between rooms.
 * Includes locking logic and limited attempts.
 *
 * @author truishi
 * @author peytonlaudanski
 * @version 2
 */
public class TriviaDoor implements Serializable {

    private static final long serialVersionUID = 2L;

    private AbstractTriviaQuestion myQuestion;
    private DoorState myState;

    private final int myMaxAttempts;
    private int myAttemptsUsed;

    public TriviaDoor(final AbstractTriviaQuestion theQuestion,
                      final DoorState theState) {

        this(theQuestion, theState, 3);
    }

    public TriviaDoor(final AbstractTriviaQuestion theQuestion,
                      final DoorState theState,
                      final int theMaxAttempts) {

        if (theState == null) {
            throw new IllegalArgumentException("Door state cannot be null");
        }

        myQuestion = theQuestion;
        myState = theState;

        myMaxAttempts = Math.max(1, theMaxAttempts);
        myAttemptsUsed = 0;
    }

    public DoorState getState() {
        return myState;
    }

    public void setState(final DoorState theState) {

        if (theState == null) {
            throw new IllegalArgumentException("Door state cannot be null");
        }

        myState = theState;
    }

    public AbstractTriviaQuestion getQuestion() {
        return myQuestion;
    }

    public boolean isLocked() {
        return myState == DoorState.LOCKED;
    }

    public void lock() {
        myState = DoorState.LOCKED;
    }

    public void unlock() {
        myState = DoorState.CLOSED;
    }

    public boolean attemptUnlock(final String theAnswer) {

        if (!isLocked()) {
            return true;
        }

        if (myAttemptsUsed >= myMaxAttempts) {
            return false;
        }

        if (myQuestion == null || theAnswer == null) {
            return false;
        }

        final boolean correct = myQuestion.checkAnswer(theAnswer);

        if (correct) {
            unlock();
            return true;
        } else {
            myAttemptsUsed++;
            return false;
        }
    }

    public int getAttemptsRemaining() {
        return Math.max(0, myMaxAttempts - myAttemptsUsed);
    }

    public int getAttemptsUsed() {
        return myAttemptsUsed;
    }

    public int getMaxAttempts() {
        return myMaxAttempts;
    }
}