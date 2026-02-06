package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class RoomDoor {

    /**
     * 
     */
    private boolean myLocked;

    // i dont know what this is supposed to be in the uml diagram so i have it, im not supposed
    // to be able to do it, so its commented out

    // private final AbstractQuestion myQuestion = new AbstractQuestion();

    /**
     * 
     * @param theLocked
     */
    public RoomDoor(final boolean theLocked) {

        setLocked(theLocked);

    }

    /**
     * 
     */
    public void lock() {
        myLocked = true;
    }

    /**
     * 
     * @return
     */
    public boolean isMyLocked() {
        return myLocked;
    }

    /**
     * 
     * @param theLocked
     */
    private void setLocked(final boolean theLocked) {
        myLocked = theLocked;
    }
}
