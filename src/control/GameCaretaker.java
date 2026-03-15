package control;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.GameState;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class GameCaretaker implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private static final String FILE_NAME = "game.ser";

    /**
     *   
     */
    private GameState myGame;

    /**
     * 
     */
    public GameCaretaker(final GameState theGame) {
        setGame(theGame);
    }

    /**
     * 
     * @param theGame
     */
    public void save() {

        final String filename = FILE_NAME;

        try (OutputStream file = Files.newOutputStream(Paths.get(filename));
                ObjectOutputStream out = new ObjectOutputStream(file);) {

            out.writeObject(myGame);
            out.close();
            file.close();
        } catch (final IOException e) {
            System.out.println(e);

        }

    }

    /**
     * 
     * @return
     */
    public GameState load() {

        GameState game = null;
        final String filename = FILE_NAME;

        try (InputStream file = Files.newInputStream(Paths.get(filename));
                ObjectInputStream input = new ObjectInputStream(file)) {

            game = (GameState) input.readObject();

            input.close();
            file.close();
        } catch (final IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return game;
    }

    /**
     * 
     * @return
     */
    public GameState getGame() {
        return myGame;
    }

    /**
     * 
     * @param theGame
     */
    private void setGame(final GameState theGame) {
        myGame = theGame;
    }

}
