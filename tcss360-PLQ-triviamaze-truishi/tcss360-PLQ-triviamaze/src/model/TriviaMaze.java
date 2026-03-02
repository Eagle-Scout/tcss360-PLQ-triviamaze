package model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * TriviaMaze
 *
 * Represents the maze grid and handles player movement.
 *
 * Rules (Version 2):
 * - Current room must have a door in the chosen direction.
 * - Outgoing door must not be LOCKED.
 * - Destination room must exist.
 * - Destination room must have the opposite door back.
 * - Incoming door must not be LOCKED.
 *
 * When answering:
 * - Correct: OPEN both sides and move.
 * - Incorrect: LOCK both sides and do not move.
 *
 * @author truishi
 * @author peytonlaudanski
 * @version 2
 */
public class TriviaMaze implements Serializable {

    private static final long serialVersionUID = 2L;

    private TriviaRoom[][] myRooms;
    private Player myPlayer;

    private int myExitX;
    private int myExitY;

    public TriviaMaze(final TriviaRoom[][] theRooms,
                      final Player thePlayer,
                      final int theExitX,
                      final int theExitY) {

        setRooms(theRooms);
        setPlayer(thePlayer);
        setExit(theExitX, theExitY);
    }

    /**
     * Called after a player answers a question for a door in a direction.
     *
     * @param theDir direction the player is trying to go
     * @param theCorrect whether the player's answer was correct
     */
    public void attemptMove(final Direction theDir, final Boolean theCorrect) {

        if (theDir == null || myPlayer == null) {
            return;
        }

        final TriviaRoom current = getCurrentRoom();
        if (current == null) {
            return;
        }

        if (!current.hasDoor(theDir)) {
            return;
        }

        final TriviaDoor outgoing = current.getDoor(theDir);
        if (outgoing == null || isDoorLocked(outgoing)) {
            return;
        }

        final int nextX = computeNextX(theDir);
        final int nextY = computeNextY(theDir);

        final TriviaRoom next = getRoom(nextX, nextY);
        if (next == null) {
            return;
        }

        final Direction opposite = theDir.opposite();
        final TriviaDoor incoming = next.getDoor(opposite);
        if (incoming == null || isDoorLocked(incoming)) {
            return;
        }

        if (Boolean.TRUE.equals(theCorrect)) {
            // Correct answer: open both sides, then move (if still valid).
            setDoorOpenBothSides(outgoing, incoming);
            movePlayer(theDir);
        } else {
            // Incorrect answer: lock both sides, no movement.
            setDoorLockedBothSides(outgoing, incoming);
        }
    }

    /**
     * Attempts to move the player one room in the given direction.
     * This method performs ALL movement validation checks.
     *
     * @param theDir direction to move
     */
    private void movePlayer(final Direction theDir) {

        if (theDir == null || myPlayer == null) {
            return;
        }

        final TriviaRoom current = getCurrentRoom();
        if (current == null) {
            return;
        }

        if (!current.hasDoor(theDir)) {
            return;
        }

        final TriviaDoor outgoing = current.getDoor(theDir);
        if (outgoing == null || isDoorLocked(outgoing)) {
            return;
        }

        final int nextX = computeNextX(theDir);
        final int nextY = computeNextY(theDir);

        final TriviaRoom next = getRoom(nextX, nextY);
        if (next == null) {
            return;
        }

        // Must have opposite door and it must not be locked either.
        final Direction opposite = theDir.opposite();
        final TriviaDoor incoming = next.getDoor(opposite);

        if (incoming == null || isDoorLocked(incoming)) {
            return;
        }

        myPlayer.move(theDir);

        if (isExit()) {
            System.out.println("You Win!");
        }
    }

    private boolean isDoorLocked(final TriviaDoor theDoor) {
        return theDoor != null && theDoor.getState() == DoorState.LOCKED;
    }

    private int computeNextX(final Direction theDir) {
        return myPlayer.getX() + theDir.getDX();
    }

    private int computeNextY(final Direction theDir) {
        return myPlayer.getY() + theDir.getDY();
    }

    private void setDoorOpenBothSides(final TriviaDoor theOutgoing, final TriviaDoor theIncoming) {
        theOutgoing.setState(DoorState.OPEN);
        theIncoming.setState(DoorState.OPEN);
    }

    private void setDoorLockedBothSides(final TriviaDoor theOutgoing, final TriviaDoor theIncoming) {
        theOutgoing.setState(DoorState.LOCKED);
        theIncoming.setState(DoorState.LOCKED);
    }

    public boolean isExit() {
        return myPlayer != null && myPlayer.getX() == myExitX && myPlayer.getY() == myExitY;
    }

    public TriviaRoom getCurrentRoom() {
        if (myPlayer == null) {
            return null;
        }
        return getRoom(myPlayer.getX(), myPlayer.getY());
    }

    /**
     * Returns the room at x,y or null if out of bounds.
     */
    public TriviaRoom getRoom(final int theX, final int theY) {

        if (myRooms == null) {
            return null;
        }

        if (theY < 0 || theY >= myRooms.length) {
            return null;
        }

        if (myRooms[theY] == null) {
            return null;
        }

        if (theX < 0 || theX >= myRooms[theY].length) {
            return null;
        }

        return myRooms[theY][theX];
    }

    public TriviaRoom[][] getRooms() {
        final TriviaRoom[][] copy = new TriviaRoom[myRooms.length][];
        for (int i = 0; i < myRooms.length; i++) {
            copy[i] = Arrays.copyOf(myRooms[i], myRooms[i].length);
        }
        return copy;
    }

    private void setRooms(final TriviaRoom[][] theRooms) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
    }

    public Player getPlayer() {
        return myPlayer;
    }

    private void setPlayer(final Player thePlayer) {
        myPlayer = thePlayer;
    }

    public int getExitX() {
        return myExitX;
    }

    public int getExitY() {
        return myExitY;
    }

    private void setExit(final int theExitX, final int theExitY) {
        myExitX = theExitX;
        myExitY = theExitY;
    }
}