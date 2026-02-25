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
    private TriviaRoom[][] myRooms;

    /**
     * 
     */
    private Player myPlayer;

    /**
     * 
     */
    private int myExitX;

    /**
     * 
     */
    private int myExitY;

    /**
     * 
     * @param theRooms
     * @param thePlayer
     * @param theExitX
     * @param theExitY
     */
    public TriviaMaze(final TriviaRoom[][] theRooms, final Player thePlayer,
            final int theExitX, final int theExitY) {
        setRooms(theRooms);
        setPlayer(thePlayer);
        setExit(theExitX, theExitY);

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
                door.setState(DoorState.OPEN);

                movePlayer(theDir);
            } else {
                door.setState(DoorState.LOCKED);
            }
            // } else {
            // System.out.println("no door that way");
        }
    }

    /**
     * 
     * @param theDir
     */
    private void movePlayer(final Direction theDir) {

        final int newX = myPlayer.getX() + theDir.getDX();
        final int newY = myPlayer.getY() + theDir.getDY();
        final TriviaRoom next = getRoom(newY, newX);

        if (next != null) {
            myPlayer.move(theDir);

            if (isExit()) {

                // win condition
                System.out.println("you win");
            }
        }

    }

    /**
     * 
     * @return
     */
    public boolean isExit() {

        return myPlayer.getX() == myExitX && myPlayer.getY() == myExitY;

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
     * @param thePlayer
     */
    private void setPlayer(final Player thePlayer) {
        myPlayer = thePlayer;
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
     * @param theExitX
     * @param theExitY
     */
    private void setExit(final int theExitX, final int theExitY) {
        myExitX = theExitX;
        myExitY = theExitY;
    }

    /**
     * 
     * @param theRooms
     */
    private void setRooms(final TriviaRoom[][] theRooms) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
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

}
