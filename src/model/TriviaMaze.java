package model;

import java.util.Arrays;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TriviaMaze {

    /**
     * 
     */
    private TriviaRoom[][] myRooms;

    /**
     * 
     */
    private Player myPlayer;

    /**
     * 
     */
    public TriviaMaze(final TriviaRoom[][] theRooms, final Player thePlayer) {
        setRooms(theRooms);
        setPlayer(thePlayer);
    }

    /**
     * 
     * @param theDir
     */
    public void movePlayer(final Direction theDir) {

        final TriviaRoom current = getCurrentRoom();

        TriviaRoom next = null;

        if (current != null && current.hasDoor(theDir)) {
            final int newX = myPlayer.getX() + theDir.getDX();
            final int newY = myPlayer.getY() + theDir.getDY();
            next = getRoom(newX, newY);

        }

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

        if (theY > 0 && theY < myRooms.length && theX > 0 && theX < myRooms[theY].length) {
            room = myRooms[theX][theY];
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
        return Arrays.copyOf(myRooms, myRooms.length);
    }

    /**
     * 
     * @param theRooms
     */
    private void setRooms(final TriviaRoom[][] theRooms) {
        myRooms = theRooms;
    }

    /**
     * 
     * @return
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * 
     * @param theRooms
     */
    private void setPlayer(final Player theRooms) {
        myPlayer = theRooms;
    }

}
