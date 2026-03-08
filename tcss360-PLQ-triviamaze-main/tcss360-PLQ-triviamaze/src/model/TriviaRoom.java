package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * 
 * @author peytonlaudanski, huyle, Quinn
 * @version 2
 */
public class TriviaRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private final Map<Direction, TriviaDoor> myDoors;

    /**
     * Fixed: Removed throw on empty/null map. MazeGenerator creates rooms empty first then
     * adds doors via addDoor(), so throwing here crashed maze generation.
     * 
     * @param theDoors
     */
    public TriviaRoom(final Map<Direction, TriviaDoor> theDoors) {
        myDoors = new EnumMap<>(Direction.class);
        if (theDoors != null) {
            myDoors.putAll(theDoors);
        }
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
     * @param theDoor
     */
    public void addDoor(final Direction theDir, final TriviaDoor theDoor) {
        myDoors.put(theDir, theDoor);
    }

    /**
     * 
     * @param theDir
     * @return
     */
    public AbstractTriviaQuestion getQuestion(final Direction theDir) {
        final TriviaDoor door = getDoor(theDir);

        return door.getQuestion();
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

        return Collections.unmodifiableMap(myDoors);
    }

}
