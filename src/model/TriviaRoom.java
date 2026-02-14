package model;

import java.util.Collections;
import java.util.EnumMap;
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
    private final Map<Direction, TriviaDoor> myDoors;

    /**
     * 
     * @param theDoors
     */
    public TriviaRoom(final Map<Direction, TriviaDoor> theDoors) {
        if (theDoors == null || theDoors.isEmpty()) {
            throw new IllegalArgumentException("Room must have at least one door");
        }
        myDoors = new EnumMap<>(theDoors);
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
