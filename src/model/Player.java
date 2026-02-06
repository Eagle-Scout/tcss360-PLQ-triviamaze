package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class Player {

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
     * @param theX
     * @param theY
     */
    public Player(final int theX, final int theY) {

        setX(theX);
        setY(theY);
    }

    /**
     * 
     * @param theDir
     */
    public void move(final Direction theDir) {

        myX += theDir.getDX();
        myY += theDir.getDY();

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
}
