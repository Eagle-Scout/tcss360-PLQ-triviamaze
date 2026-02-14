package model;

/**
 * /** Directions in the maze coordinate system. Note: Y-axis increases downward (SOUTH),
 * similar to 2D array indexing.
 * 
 * @author peytonlaudanski
 * @version 1
 */

public enum Direction {

    /**
     * NORTH(0, -1) - Decreases Y (moves UP in the array).
     */
    NORTH(0, -1),

    /**
     * SOUTH(0, 1) - Increases Y (moves DOWN in the array).
     */
    SOUTH(0, 1),

    /**
     * EAST(1, 0) - Increases X (moves RIGHT in the array).
     */
    EAST(1, 0),

    /**
     * WEST(-1, 0) - Decreases X (moves LEFT in the array).
     */
    WEST(-1, 0);

    /**
     * 
     */
    private final int myDX;

    /**
     * 
     */
    private final int myDY;

    /**
    * 
    */
    Direction(final int theDX, final int theDY) {

        myDX = theDX;
        myDY = theDY;

    }

    /**
     * 
     * @return
     */
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    /**
     * 
     * @return
     */
    public int getDY() {
        return myDY;
    }

    /**
     * 
     * @return
     */
    public int getDX() {
        return myDX;
    }

}