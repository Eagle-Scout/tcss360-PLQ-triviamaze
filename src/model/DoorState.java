package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public enum DoorState {

    /**
     * 
     */
    OPEN("Open"),

    /**
     * 
     */
    CLOSED("Closed"),

    /**
     * 
     */
    LOCKED("Locked");

    /**
     * 
     */
    private final String myState;

    /**
     * 
     * @param theState
     */
    DoorState(final String theState) {
        myState = theState;
    }

    /**
     * 
     * @return
     */
    public String getDoorState() {
        return myState;

    }
}
