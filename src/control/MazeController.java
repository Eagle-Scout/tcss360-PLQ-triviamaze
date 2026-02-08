package control;

import model.Direction;
import model.TriviaMaze;
import view.MazeView;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class MazeController {

    /**
     * 
     */
    private TriviaMaze myMaze;

    /**
     * 
     */
    private MazeView myView;

    /**
     * 
     * @param theMaze
     * @param theView
     */
    public MazeController(final TriviaMaze theMaze, final MazeView theView) {
        setMaze(theMaze);
        setView(theView);
    }

    /**
     * 
     */
    public void saveGame() {

    }

    /**
     * 
     */
    public void loadGame() {

    }

    /**
     * 
     * @param theView
     */
    private void setView(final MazeView theView) {
        myView = theView;
    }

    /**
     * 
     * @param theMaze
     */
    private void setMaze(final TriviaMaze theMaze) {
        myMaze = theMaze;
    }

    /**
     * 
     * @return
     */
    public MazeView getMyView() {
        return myView;
    }

    /**
     * 
     * @return
     */
    public TriviaMaze getMyMaze() {
        return myMaze;
    }

    /**
     * 
     * @param theArgs
     */
    public static void main(final String[] theArgs) {

        final TriviaMaze maze = new TriviaMaze(null, null);

        maze.movePlayer(Direction.NORTH);

    }
}
