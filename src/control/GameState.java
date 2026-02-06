package control;

import java.util.List;

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
    private List<Integer> myDoors;

    /**
     * 
     * @param theX
     * @param theY
     * @param theDoors
     */
    public GameState(final int theX, final int theY, final List<Integer> theDoors) {
        setX(theX);
        setY(theY);
        setLockedDoors(theDoors);
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
    public List<Integer> getMyLockedDoors() {
        return myDoors;
    }

    /**
     * 
     * @param theDoors
     */
    private void setLockedDoors(final List<Integer> theDoors) {
        myDoors = theDoors;
    }

}
