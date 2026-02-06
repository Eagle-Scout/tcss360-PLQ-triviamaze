package control;

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
        setMaze(new TriviaMaze());
        setView(new MazeView());

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

}
