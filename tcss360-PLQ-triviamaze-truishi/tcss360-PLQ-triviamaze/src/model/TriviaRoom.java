package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * 
 * @author peytonlaudanski
 * @version 2
 */
public class TriviaRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Direction, TriviaDoor> myDoors;

    /**
     * Constructs a room with the given door map.
     * Rooms may start with an empty door map (maze generation adds doors later).
     *
     * @param theDoors door map (may be empty, but not null)
     */
    public TriviaRoom(final Map<Direction, TriviaDoor> theDoors) {
        if (theDoors == null) {
            throw new IllegalArgumentException("Door map cannot be null");
        }

        // Rooms created by the maze generator start with no doors.
        // Doors are added later as the maze is carved.
        myDoors = new EnumMap<>(theDoors);
    }

    public TriviaDoor getDoor(final Direction theDir) {
        return myDoors.get(theDir);
    }

    public void addDoor(final Direction theDir, final TriviaDoor theDoor) {
        myDoors.put(theDir, theDoor);
    }

    public AbstractTriviaQuestion getQuestion(final Direction theDir) {
        final TriviaDoor door = getDoor(theDir);
        return door.getQuestion();
    }

    public boolean hasDoor(final Direction theDir) {
        return myDoors.containsKey(theDir);
    }

    public Map<Direction, TriviaDoor> getDoors() {
        return Collections.unmodifiableMap(myDoors);
    }
}