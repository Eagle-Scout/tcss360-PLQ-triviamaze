package control;

import java.io.Serializable;

import model.Player;
import model.TriviaMaze;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Player myPlayer;

    /**
     * 
     */
    private TriviaMaze myMaze;

    /**
     * 
     * @param theX
     * @param theY
     * @param theDoors
     */
    public GameState(final Player thePlayer, final TriviaMaze theMaze) {

        setPlayer(thePlayer);
        setMaze(theMaze);
    }

    /**
     * 
     * @return
     */
    public TriviaMaze getMaze() {
        return myMaze;
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
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * 
     * @param thePlayer
     */
    private void setPlayer(final Player thePlayer) {
        myPlayer = thePlayer;
    }

}
