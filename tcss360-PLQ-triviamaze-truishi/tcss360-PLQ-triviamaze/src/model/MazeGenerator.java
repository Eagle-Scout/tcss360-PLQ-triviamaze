package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

/**
 * MazeGenerator
 *
 * Generates a random maze using a depth-first search (DFS) carve algorithm.
 * Rooms start empty (no doors) and the generator adds shared door objects
 * between adjacent rooms so that both rooms reference the same door instance.
 *
 * Notes:
 * - Minimum maze size is 4x4.
 * - After carving a perfect maze (one unique path between any two cells),
 *   we optionally add extra random connections to create multiple paths.
 *
 * @author peytonlaudanski
 * @author truishi
 * @version 2
 */
public class MazeGenerator {

    /** Maze height (rows). */
    private final int myHeight;

    /** Maze width (columns). */
    private final int myWidth;

    /** Probability of adding an extra door (non-tree edge). */
    private static final double EXTRA_DOOR_PROBABILITY = 0.2;

    /**
     * Creates a maze generator.
     *
     * @param theHeight desired height
     * @param theWidth desired width
     */
    public MazeGenerator(final int theHeight, final int theWidth) {
        final int minMazeSize = 4;

        myHeight = Math.max(minMazeSize, theHeight);
        myWidth = Math.max(minMazeSize, theWidth);
    }

    /**
     * Generates a new maze.
     *
     * @param thePlayer the player instance to place in the maze
     * @param theExitX exit x coordinate
     * @param theExitY exit y coordinate
     * @return a TriviaMaze with a generated room grid
     */
    public TriviaMaze generateMaze(final Player thePlayer, final int theExitX, final int theExitY) {

        final TriviaRoom[][] rooms = createEmptyRooms();

        carveMaze(0, 0, rooms, new boolean[myHeight][myWidth]);
        addMoreDoors(rooms);

        return new TriviaMaze(rooms, thePlayer, theExitX, theExitY);
    }

    /**
     * Creates a grid of rooms with no doors.
     */
    private TriviaRoom[][] createEmptyRooms() {
        final TriviaRoom[][] rooms = new TriviaRoom[myHeight][myWidth];
        for (int y = 0; y < myHeight; y++) {
            for (int x = 0; x < myWidth; x++) {
                rooms[y][x] = new TriviaRoom(new EnumMap<>(Direction.class));
            }
        }
        return rooms;
    }

    /**
     * DFS carve step.
     */
    private void carveMaze(final int theX, final int theY, final TriviaRoom[][] theRooms,
            final boolean[][] theVisitedRooms) {

        theVisitedRooms[theY][theX] = true;

        final List<Direction> dirs = Arrays.asList(Direction.values());
        Collections.shuffle(dirs);

        for (final Direction dir : dirs) {
            final int newX = theX + dir.getDX();
            final int newY = theY + dir.getDY();

            if (inBounds(newX, newY) && !theVisitedRooms[newY][newX]) {

                // Shared door between (theX,theY) and (newX,newY)
                final TriviaDoor sharedDoor =
                        new TriviaDoor(QuestionFactory.getQuestion(), DoorState.CLOSED);

                theRooms[theY][theX].addDoor(dir, sharedDoor);
                theRooms[newY][newX].addDoor(dir.opposite(), sharedDoor);

                carveMaze(newX, newY, theRooms, theVisitedRooms);
            }
        }
    }

    /**
     * Adds additional random connections between adjacent rooms.
     */
    private void addMoreDoors(final TriviaRoom[][] theRooms) {

        for (int row = 0; row < theRooms.length; row++) {
            final List<Direction> dirs = Arrays.asList(Direction.values());

            for (int column = 0; column < theRooms[row].length; column++) {
                Collections.shuffle(dirs);

                for (final Direction dir : dirs) {
                    final int nextRow = row + dir.getDY();
                    final int nextCol = column + dir.getDX();

                    if (inBounds(nextCol, nextRow)
                            && !theRooms[row][column].hasDoor(dir)
                            && Math.random() < EXTRA_DOOR_PROBABILITY) {

                        final TriviaDoor sharedDoor =
                                new TriviaDoor(QuestionFactory.getQuestion(), DoorState.CLOSED);

                        theRooms[row][column].addDoor(dir, sharedDoor);
                        theRooms[nextRow][nextCol].addDoor(dir.opposite(), sharedDoor);
                    }
                }
            }
        }
    }

    /**
     * Bounds check.
     */
    private boolean inBounds(final int theX, final int theY) {
        return theY >= 0 && theY < myHeight && theX >= 0 && theX < myWidth;
    }
}