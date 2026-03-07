package model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author peytonlaudanski, huyle
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
