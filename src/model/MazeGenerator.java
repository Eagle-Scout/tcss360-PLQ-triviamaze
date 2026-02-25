package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
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
        final TriviaDoor sharedDoor = new TriviaDoor(null, null);

        for (final Direction dir : dirs) {
            final int newX = theX + dir.getDX();
            final int newY = theY + dir.getDY();

            if (inBounds(newX, newY) && !theVisitedRooms[newY][newX]) {

                sharedDoor.setState(DoorState.CLOSED);
                sharedDoor.setQuestion(QuestionFactory.getQuestion());

                theRooms[theY][theX].addDoor(dir, sharedDoor);
                theRooms[newY][newX].addDoor(dir.opposite(), sharedDoor);

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
        final TriviaDoor sharedDoor = new TriviaDoor(null, null);
        final double probablility = 0.2;

        for (int row = 0; row < theRooms.length; row++) {
            final List<Direction> dirs = Arrays.asList(Direction.values());

            for (int column = 0; column < theRooms[row].length; column++) {
                Collections.shuffle(dirs);

                for (final Direction dir : dirs) {
                    final int nextRow = row + dir.getDY();
                    final int nextCol = column + dir.getDX();

                    if (inBounds(nextCol, nextRow) && !theRooms[row][column].hasDoor(dir)
                            && Math.random() < probablility) {

                        sharedDoor.setQuestion(QuestionFactory.getQuestion());
                        sharedDoor.setState(DoorState.CLOSED);
                        theRooms[row][column].addDoor(dir, sharedDoor);
                        theRooms[nextRow][nextCol].addDoor(dir.opposite(), sharedDoor);
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
