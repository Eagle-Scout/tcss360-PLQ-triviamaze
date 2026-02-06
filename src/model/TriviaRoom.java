package model;

import java.util.Map;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TriviaRoom {

    /**
     * 
     */
    private Map<Direction, TriviaDoor> myDoors;

    /**
     * 
     * @param theDoors
     */
    public TriviaRoom(final Map<Direction, TriviaDoor> theDoors) {
        setDoors(theDoors);

    }

    /**
     * 
     * @param theDir
     * @return
     */
    public TriviaDoor getDoor(final Direction theDir) {

        return myDoors.get(theDir);
    }

    /**
     * 
     * @param theDir
     * @return
     */
    public boolean hasDoor(final Direction theDir) {
        return myDoors.containsKey(theDir);
    }

    /**
     * 
     * @return
     */
    public Map<Direction, TriviaDoor> getDoors() {

        return myDoors;
    }

    /**
     * 
     * @param theDoors
     */
    private void setDoors(final Map<Direction, TriviaDoor> theDoors) {
        myDoors = theDoors;
    }

}
