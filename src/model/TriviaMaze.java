package model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TriviaMaze implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private final TriviaRoom[][] myRooms;

    /**
     * 
     */
    private final Player myPlayer;

    /**
     * 
     */
    public TriviaMaze(final TriviaRoom[][] theRooms, final Player thePlayer) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
        myPlayer = thePlayer;

    }

    /**
     * 
     * @param theDir
     * @param theBool
     */
    public void attemptMove(final Direction theDir, final Boolean theBool) {
        final TriviaRoom current = getCurrentRoom();

        if (current != null && current.hasDoor(theDir)) {

            final TriviaDoor door = current.getDoor(theDir);

            if (theBool) {
                movePlayer(theDir);
            } else {
                door.lock();
            }
            // } else {
            // System.out.println("no door that way");
        }
    }

    /**
     * 
     * @param theDir
     */
    public void movePlayer(final Direction theDir) {

        final int newX = myPlayer.getX() + theDir.getDX();
        final int newY = myPlayer.getY() + theDir.getDY();
        final TriviaRoom next = getRoom(newX, newY);

        if (next != null && next.getDoor(theDir.opposite()) != null) {
            myPlayer.move(theDir);

        }

    }

    /**
     * 
     * @param theX
     * @param theY
     * @return
     */
    public TriviaRoom getRoom(final int theX, final int theY) {
        TriviaRoom room = null;

        if (theY >= 0 && theY < myRooms.length && theX >= 0 && theX < myRooms[theY].length) {
            room = myRooms[theY][theX];
        }

        return room;
    }

    /**
     * 
     * @return
     */
    public TriviaRoom getCurrentRoom() {
        return getRoom(myPlayer.getX(), myPlayer.getY());
    }

    /**
     * 
     * @param theX
     * @param theY
     * @return
     */
    public boolean isExit(final int theX, final int theY) {
        return false;
    }

    /**
     * 
     * @return
     */
    public TriviaRoom[][] getRooms() {
        final TriviaRoom[][] copy = new TriviaRoom[myRooms.length][];
        for (int i = 0; i < myRooms.length; i++) {
            copy[i] = Arrays.copyOf(myRooms[i], myRooms[i].length);

        }
        return copy;

    }

    /**
     * 
     * @return
     */
    public Player getPlayer() {
        return myPlayer;
    }

}
