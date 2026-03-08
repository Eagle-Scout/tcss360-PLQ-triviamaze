package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

/**
 *
 * @author peytonlaudanski, huyle
 * @version 2
 */
public class MazeGenerator {

    /**
     *
     */
    private int myHeight;

    /**
     *
     */
    private int myWidth;

    /**
     *
     * @param theHeight
     * @param theWidth
     */
    public MazeGenerator(final int theHeight, final int theWidth) {
        final int minMazeSize = 4;

        myHeight = minMazeSize;
        myWidth = minMazeSize;

        if (theHeight > minMazeSize) {
            myHeight = theHeight;
        }
        if (theWidth > minMazeSize) {
            myWidth = theWidth;
        }
    }

    /**
     *
     * @param thePlayer
     * @param theExitX
     * @param theExitY
     * @return
     */
    public TriviaMaze generateMaze(final Player thePlayer, final int theExitX,
            final int theExitY) {

        final TriviaRoom[][] rooms = createEmptyRooms();
        carveMaze(0, 0, rooms, new boolean[myHeight][myWidth]);
        return new TriviaMaze(rooms, thePlayer, theExitX, theExitY);
    }

    /**
     *
     * @return
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
     *
     * @param theX
     * @param theY
     * @param theRooms
     * @param theVisitedRooms
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
                // Fixed: Create a fresh door for each passage so state changes only affect the
                // one door, not every door in the maze.
                final TriviaDoor door =
                        new TriviaDoor(QuestionFactory.getQuestion(), DoorState.CLOSED);
                theRooms[theY][theX].addDoor(dir, door);
                theRooms[newY][newX].addDoor(dir.opposite(), door);
                carveMaze(newX, newY, theRooms, theVisitedRooms);
            }
        }
        addMoreDoors(theRooms);
    }

    /**
     *
     * @param theRooms
     */
    private void addMoreDoors(final TriviaRoom[][] theRooms) {
        final double probability = 0.005;

        for (int row = 0; row < theRooms.length; row++) {
            final List<Direction> dirs = Arrays.asList(Direction.values());

            for (int col = 0; col < theRooms[row].length; col++) {
                Collections.shuffle(dirs);

                for (final Direction dir : dirs) {
                    final int nextRow = row + dir.getDY();
                    final int nextCol = col + dir.getDX();

                    if (inBounds(nextCol, nextRow) && !theRooms[row][col].hasDoor(dir)
                            && Math.random() < probability) {
                        // Fixed: Fresh door each time.
                        final TriviaDoor door = new TriviaDoor(QuestionFactory.getQuestion(),
                                DoorState.CLOSED);
                        theRooms[row][col].addDoor(dir, door);
                        theRooms[nextRow][nextCol].addDoor(dir.opposite(), door);
                    }
                }
            }
        }
    }

    /**
     *
     * @param theX
     * @param theY
     * @return
     */
    private boolean inBounds(final int theX, final int theY) {
        return theY >= 0 && theY < myHeight && theX >= 0 && theX < myWidth;
    }
}