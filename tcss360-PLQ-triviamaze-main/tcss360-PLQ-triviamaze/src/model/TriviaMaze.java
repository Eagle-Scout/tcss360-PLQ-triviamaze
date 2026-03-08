package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author peytonlaudanski, huyle, Quinn
 * @version 2
 */
public class TriviaMaze implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private TriviaRoom[][] myRooms;

    /**
     * 
     */
    private Player myPlayer;

    /**
     * 
     */
    private int myExitX;

    /**
     * 
     */
    private int myExitY;

    /**
     * 
     * @param theRooms
     * @param thePlayer
     * @param theExitX
     * @param theExitY
     */
    public TriviaMaze(final TriviaRoom[][] theRooms, final Player thePlayer,
            final int theExitX, final int theExitY) {
        setRooms(theRooms);
        setPlayer(thePlayer);
        setExit(theExitX, theExitY);

    }

    /**
     * 
     * @param theDir
     * @param theBool
     */
    public void attemptMove(final Direction theDir, final Boolean theBool) {
        final TriviaRoom current = getCurrentRoom();

        if (current != null && current.hasDoor(theDir)) {

            final TriviaDoor door = current.getDoor(theDir);

            if (theBool) {
                door.setState(DoorState.OPEN);

                movePlayer(theDir);
            } else {
                door.setState(DoorState.LOCKED);
            }
            // } else {
            // System.out.println("no door that way");
        }
    }

    /**
     * 
     * @param theDir
     */
    private void movePlayer(final Direction theDir) {

        final int newX = myPlayer.getX() + theDir.getDX();
        final int newY = myPlayer.getY() + theDir.getDY();
        final TriviaRoom next = getRoom(newY, newX);

        if (next != null) {
            myPlayer.move(theDir);

            if (isExit()) {

                // win condition
                System.out.println("you win");
            }
        }

    }

    /**
     * Checks whether the player can still reach the exit from the current room.
     * DFS treats LOCKED doors as blocked, and OPEN/CLOSED doors as traversable
     * because closed doors can still be answered and used.
     * 
     * @return true if a path to the exit still exists; false otherwise.
     */
    public boolean canLeaveMaze() {
        final Set<String> visited = new HashSet<>();
        return canLeaveMaze(myPlayer.getX(), myPlayer.getY(), visited);
    }

    /**
     * Depth-first search helper that explores every non-locked path from the
     * current room to the exit.
     * 
     * @param theX current x position
     * @param theY current y position
     * @param theVisited set of visited room coordinates
     * @return true if the exit is reachable from this room
     */
    private boolean canLeaveMaze(final int theX, final int theY,
            final Set<String> theVisited) {

        if (theX == myExitX && theY == myExitY) {
            return true;
        }

        final String key = theX + "," + theY;
        if (theVisited.contains(key)) {
            return false;
        }
        theVisited.add(key);

        final TriviaRoom current = getRoom(theY, theX);
        if (current == null) {
            return false;
        }

        for (final Direction dir : Direction.values()) {
            if (current.hasDoor(dir)) {
                final TriviaDoor door = current.getDoor(dir);
                if (door.getState() != DoorState.LOCKED) {
                    final int nextX = theX + dir.getDX();
                    final int nextY = theY + dir.getDY();
                    if (getRoom(nextY, nextX) != null
                            && canLeaveMaze(nextX, nextY, theVisited)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 
     * @return
     */
    public boolean isExit() {

        return myPlayer.getX() == myExitX && myPlayer.getY() == myExitY;

    }

    /**
     * 
     * @param theX
     * @param theY
     * @return
     */
    public TriviaRoom getRoom(final int theRow, final int theCol) {

        TriviaRoom room = null;

        if (theRow >= 0 && theRow < myRooms.length && theCol >= 0
                && theCol < myRooms[theRow].length) {
            room = myRooms[theRow][theCol];
        }
        return room;
    }

    /**
     * Fixed: This was getRoom(X, Y) which swapped row and column. Array is [row][col] = [y][x]
     * so must pass Y first, then X.
     * 
     * @return
     */
    public TriviaRoom getCurrentRoom() {
        return getRoom(myPlayer.getY(), myPlayer.getX());
    }

    /**
     * 
     * @param thePlayer
     */
    private void setPlayer(final Player thePlayer) {
        myPlayer = thePlayer;
    }

    /**
     * 
     * @return
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * 
     * @param theExitX
     * @param theExitY
     */
    private void setExit(final int theExitX, final int theExitY) {
        myExitX = theExitX;
        myExitY = theExitY;
    }

    /**
     * 
     * @param theRooms
     */
    private void setRooms(final TriviaRoom[][] theRooms) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
    }

    /**
     * 
     * @return
     */
    public TriviaRoom[][] getRooms() {
        final TriviaRoom[][] copy = new TriviaRoom[myRooms.length][];
        for (int i = 0; i < myRooms.length; i++) {
            copy[i] = Arrays.copyOf(myRooms[i], myRooms[i].length);

        }
        return copy;

    }

    /**
     * 
     * @param theFactory
     * @return
     */
    public static TriviaMaze generateDefaultMaze(final QuestionFactory theFactory) {

        final Player player = new Player(0, 0);
        final int exitX = 3;
        final int exitY = 3;

        // Create a 4x4 maze using MazeGenerator
        final MazeGenerator generator = new MazeGenerator(4, 4);

        // Exit is bottom-right (3,3)
        return generator.generateMaze(player, exitX, exitY);
    }

}