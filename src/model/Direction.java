package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public enum Direction {
    /**
     * 
     */
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0);

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