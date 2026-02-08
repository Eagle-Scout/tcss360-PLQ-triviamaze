package model;

import java.util.Arrays;

/**
 * Represents the trivia maze and handles player movement between rooms.
 *
 * Movement rules:
 * - Player can only move if there is a door in that direction.
 * - If the outgoing door is locked, player must unlock it (attemptMovePlayer).
 * - The opposite/incoming door must exist in the next room.
 *
 * @author peytonlaudanski
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
     * Existing method name.
     * Now it will NOT move through locked doors (no answer provided).
     *
     * @param theDir direction to move
     */
    public void movePlayer(final Direction theDir) {
        attemptMovePlayer(theDir, null);
    }

    /**
     * Try to move the player. If the door is locked, an answer is required.
     *
     * @param theDir direction to move
     * @param theAnswer answer for unlocking if needed (null means "don't unlock")
     * @return true if the player moved, false otherwise
     */
    public boolean attemptMovePlayer(final Direction theDir, final String theAnswer) {

        final TriviaRoom current = getCurrentRoom();
        TriviaRoom next = null;

        if (current != null && current.hasDoor(theDir)) {

            final TriviaDoor outgoing = current.getDoor(theDir);
            if (outgoing == null) {
                return false;
            }

            // Block if locked unless we successfully unlock now.
            if (outgoing.isMyLocked()) {
                if (theAnswer == null || !outgoing.attemptUnlock(theAnswer)) {  // FIXED name
                    return false;
                }
            }

            final int newX = myPlayer.getX() + theDir.getDX();
            final int newY = myPlayer.getY() + theDir.getDY();
            next = getRoom(newX, newY);

            if (next == null) {
                return false;
            }

            // Must have a door back from the next room.
            final TriviaDoor incoming = next.getDoor(theDir.opposite());
            if (incoming == null) {
                return false;
            }

            // Keep both sides consistent: if you unlocked outgoing, unlock incoming too.
            if (incoming.isMyLocked()) {
                incoming.unlock();  // FIXED semicolon
            }

            // Final safety check.
            if (incoming.isMyLocked()) {
                return false;
            }

            myPlayer.move(theDir);
            return true;
        }

        return false;
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
