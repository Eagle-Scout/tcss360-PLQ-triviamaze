package control;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class GameCaretaker {

    /**
     *  
     */
    private GameState myGame;

    /**
     * 
     */
    public GameCaretaker(final GameState theGame) {
        setSaved(theGame);
    }

    /**
     * 
     * @param theGame
     */
    public void save(final GameState theGame) {

    }

    /**
     * 
     * @return
     */
    public GameState load() {
        return null;
    }

    /**
     * 
     * @return
     */
    public GameState getSaved() {
        return myGame;
    }

    /**
     * 
     * @param theGame
     */
    private void setSaved(final GameState theGame) {
        myGame = theGame;
    }

}
