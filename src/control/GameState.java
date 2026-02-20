package control;

import model.TriviaRoom;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class GameState {

    /**
     * 
     */
    private int myX;

    /**
     * 
     */
    private int myY;

    /**
     * 
     */
    private TriviaRoom[][] myRooms;

    /**
     * 
     * @param theX
     * @param theY
     * @param theDoors
     */
    public GameState(final int theX, final int theY, final TriviaRoom[][] theRooms) {
        setX(theX);
        setY(theY);
        setRooms(theRooms);
    }

    /**
     * 
     * @return
     */
    public int getMyX() {
        return myX;
    }

    /**
     * 
     * @param theX
     */
    private void setX(final int theX) {
        myX = theX;
    }

    /**
     * 
     * @return
     */
    public int getMyY() {
        return myY;
    }

    /**
     * 
     * @param theY
     */
    private void setY(final int theY) {
        myY = theY;
    }

    /**
     * 
     * @return
     */
    public TriviaRoom[][] getRooms() {
        return myRooms.clone();
    }

    /**
     * 
     * @param theDoors
     */
    private void setRooms(final TriviaRoom[][] theRooms) {
        myRooms = theRooms;
    }

}
