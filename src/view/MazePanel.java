package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;

import javax.swing.JPanel;

import model.Direction;
import model.TriviaDoor;
import model.TriviaRoom;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class MazePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Room border color. */
    private static final Color ROOM_BORDER = new Color(190, 190, 220);

    /** Success/open door color. */
    private static final Color SUCCESS = new Color(40, 220, 90);

    /** Warning/closed door color. */
    private static final Color WARNING = new Color(255, 200, 0);

    /** Wrong/locked door color. */
    private static final Color WRONG = new Color(220, 40, 40);

    /** Exit fill color. */
    private static final Color EXIT_FILL = new Color(255, 215, 0, 80);

    /** Exit text color. */
    private static final Color EXIT_TEXT = new Color(255, 215, 0);

    /** Player dot color. */
    private static final Color PLAYER_COLOR = new Color(0, 210, 255);

    /** Room fill color. */
    private static final Color ROOM_COLOR = new Color(55, 55, 80);

    /** Index of the largest padding value in the PADDING array. */
    private static final int PADDING_LARGE = 3;

    /***/
    private static final int HALF = 2;

    /** The current room grid. */
    private TriviaRoom[][] myRooms;

    /** Player column index. */
    private int myPlayerX;

    /** Player row index. */
    private int myPlayerY;

    /** Constructs an empty MazePanel. */
    MazePanel() {
        super();
    }

    /**
     * Sets the room grid and player position, then repaints.
     *
     * @param theRooms   the room grid
     * @param thePlayerX player column
     * @param thePlayerY player row
     */
    public void setRooms(final TriviaRoom[][] theRooms, final int thePlayerX,
            final int thePlayerY) {
        myRooms = Arrays.copyOf(theRooms, theRooms.length);
        myPlayerX = thePlayerX;
        myPlayerY = thePlayerY;
    }

    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        if (myRooms == null) {
            return;
        }

        final Graphics2D graphics = (Graphics2D) theGraphics;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        final int rows = myRooms.length;
        final int cols = myRooms[0].length;

        final int cell = Math.min((getWidth() - MazeView.PADDING[PADDING_LARGE]) / cols,
                (getHeight() - MazeView.PADDING[PADDING_LARGE]) / rows);
        final int offsetX = (getWidth() - cols * cell) / HALF;
        final int offsetY = (getHeight() - rows * cell) / HALF;

        // EXIT highlight (bottom-right)
        final int exitCol = cols - 1;
        final int exitRow = rows - 1;
        graphics.setColor(EXIT_FILL);
        graphics.fillRect(offsetX + exitCol * cell, offsetY + exitRow * cell, cell, cell);
        graphics.setColor(EXIT_TEXT);
        graphics.setFont(new Font(MazeView.FONT, Font.BOLD, cell / MazeView.RATIOS[1]));
        graphics.drawString("EXIT", offsetX + exitCol * cell + cell / MazeView.RATIOS[1],
                offsetY + exitRow * cell + cell / HALF);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawRoom(graphics, myRooms[row][col], offsetX + col * cell,
                        offsetY + row * cell, cell);
            }
        }

        // PLAYER MARKER.
        final int playerX = offsetX + myPlayerX * cell + cell / HALF;
        final int playerY = offsetY + myPlayerY * cell + cell / HALF;
        final int radius = cell / MazeView.RATIOS[1];
        graphics.setColor(PLAYER_COLOR);
        graphics.fillOval(playerX - radius, playerY - radius, radius * HALF, radius * HALF);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(MazeView.FONT, Font.BOLD, radius));
        graphics.drawString("\u2605", playerX - radius / HALF, playerY + radius / HALF);
    }

    private void drawRoom(final Graphics2D theG, final TriviaRoom theRoom, final int theX,
            final int theY, final int theSize) {
        theG.setColor(ROOM_COLOR);
        theG.fillRect(theX, theY, theSize, theSize);
        theG.setColor(ROOM_BORDER);
        theG.setStroke(new BasicStroke(MazeView.GRID.width));
        theG.drawRect(theX, theY, theSize, theSize);

        final int margin = theSize / MazeView.RATIOS[0];
        final int thick = Math.max(6, theSize / MazeView.RATIOS[HALF]);
        theG.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (theRoom != null) {
            drawAllDoors(theG, theRoom, theX, theY, theSize, margin);
        }
    }

    private void drawAllDoors(final Graphics2D theG, final TriviaRoom theRoom, final int theX,
            final int theY, final int theSize, final int theMargin) {
        if (theRoom.hasDoor(Direction.NORTH)) {
            drawDoor(theG, theX + theMargin, theY, theX + theSize - theMargin, theY,
                    theRoom.getDoor(Direction.NORTH));
        }
        if (theRoom.hasDoor(Direction.SOUTH)) {
            drawDoor(theG, theX + theMargin, theY + theSize, theX + theSize - theMargin,
                    theY + theSize, theRoom.getDoor(Direction.SOUTH));
        }
        if (theRoom.hasDoor(Direction.EAST)) {
            drawDoor(theG, theX + theSize, theY + theMargin, theX + theSize,
                    theY + theSize - theMargin, theRoom.getDoor(Direction.EAST));
        }
        if (theRoom.hasDoor(Direction.WEST)) {
            drawDoor(theG, theX, theY + theMargin, theX, theY + theSize - theMargin,
                    theRoom.getDoor(Direction.WEST));
        }
    }

    private void drawDoor(final Graphics2D theG, final int theX1, final int theY1,
            final int theX2, final int theY2, final TriviaDoor theDoor) {
        if (theDoor == null) {
            return;
        }
        final Color color = switch (theDoor.getState()) {
            case OPEN -> SUCCESS;
            case CLOSED -> WARNING;
            case LOCKED -> WRONG;
        };
        theG.setColor(color);
        theG.drawLine(theX1, theY1, theX2, theY2);
    }
}