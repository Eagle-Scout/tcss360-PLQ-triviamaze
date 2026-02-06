package model;

import java.util.Map;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class MazeRoom {

    /**
     * 
     */
    private Map<Direction, RoomDoor> myDoors;

    /**
     * 
     * @param theDoors
     */
    public MazeRoom(final Map<Direction, RoomDoor> theDoors) {
        setDoors(theDoors);

    }

    /**
     * 
     * @param theDir
     * @return
     */
    public RoomDoor getDoor(final Direction theDir) {

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
    public Map<Direction, RoomDoor> getMyDoors() {

        return myDoors;
    }

    /**
     * 
     * @param theDoors
     */
    private void setDoors(final Map<Direction, RoomDoor> theDoors) {
        myDoors = theDoors;
    }

}
