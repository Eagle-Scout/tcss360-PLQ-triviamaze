package model;

import java.util.Arrays;

/**
 * Represents the trivia maze and handles player movement between rooms.
 *
 * Door movement rules:
 * - Player can only move if the current room has a door in that direction.
 * - Player cannot move through a locked door.
 * - If a move is allowed, the destination room must exist and must have the
 *   opposite door back.
 *
 * @author peytonlaudanski, quinnweinzoff
 * @version 2
 */
public class TriviaMaze {

    private final TriviaRoom[][] myRooms;
    private final Player myPlayer;

    public TriviaMaze(final TriviaRoom[][] theRooms, final Player thePlayer) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
        myPlayer = thePlayer;
    }

    /**
     * Attempts to move based on whether the door's question was answered correctly.
     *
     * If answered correctly (theBool == true):
     * - unlock door (both sides)
     * - move player
     *
     * If answered incorrectly (theBool == false):
     * - lock door (both sides)
     * - do not move player
     *
     * @param theDir direction to move
     * @param theBool true if question answered correctly; false otherwise
     */
    public void attemptMove(final Direction theDir, final Boolean theBool) {
        final TriviaRoom current = getCurrentRoom();

        if (current == null || !current.hasDoor(theDir)) {
            return;
        }

        final TriviaDoor outgoing = current.getDoor(theDir);
        if (outgoing == null) {
            return;
        }

        final int newX = myPlayer.getX() + theDir.getDX();
        final int newY = myPlayer.getY() + theDir.getDY();
        final TriviaRoom next = getRoom(newX, newY);

        if (next == null) {
            return;
        }

        final TriviaDoor incoming = next.getDoor(theDir.opposite());
        if (incoming == null) {
            return;
        }

        if (Boolean.TRUE.equals(theBool)) {
            // Correct: unlock both sides, then move.
            outgoing.unlock();
            incoming.unlock();
            movePlayer(theDir);
        } else {
            // Incorrect: lock both sides, do not move.
            outgoing.lock();
            incoming.lock();
        }
    }

    /**
     * Moves the player if the move is valid.
     *
     * Valid move requirements:
     * - current room has a door in that direction
     * - that door is NOT locked
     * - destination room exists
     * - destination room has the opposite door back
     * - incoming (opposite) door is NOT locked
     *
     * @param theDir direction to move
     */
    public void movePlayer(final Direction theDir) {
        final TriviaRoom current = getCurrentRoom();
        if (current == null || !current.hasDoor(theDir)) {
            return;
        }

        final TriviaDoor outgoing = current.getDoor(theDir);
        if (outgoing == null || outgoing.isLocked()) {
            return; // locked door blocks movement
        }

        final int newX = myPlayer.getX() + theDir.getDX();
        final int newY = myPlayer.getY() + theDir.getDY();
        final TriviaRoom next = getRoom(newX, newY);

        if (next == null) {
            return;
        }

        final TriviaDoor incoming = next.getDoor(theDir.opposite());
        if (incoming == null || incoming.isLocked()) {
            return;
        }

        myPlayer.move(theDir);
    }

    public TriviaRoom getRoom(final int theX, final int theY) {
        TriviaRoom room = null;

        if (theY >= 0 && theY < myRooms.length && theX >= 0 && theX < myRooms[theY].length) {
            room = myRooms[theY][theX];
        }

        return room;
    }

    public TriviaRoom getCurrentRoom() {
        return getRoom(myPlayer.getX(), myPlayer.getY());
    }

    public boolean isExit(final int theX, final int theY) {
        return false;
    }

    public TriviaRoom[][] getRooms() {
        return Arrays.copyOf(myRooms, myRooms.length);
    }

    public Player getPlayer() {
        return myPlayer;
    }
}
