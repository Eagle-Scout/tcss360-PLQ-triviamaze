package control;

import java.awt.Color;

import javax.swing.SwingUtilities;

import model.Direction;
import model.DoorState;
import model.GameState;
import model.MazeGenerator;
import model.Player;
import model.QuestionFactory;
import model.TriviaDoor;
import model.TriviaMaze;
import model.TriviaRoom;
import view.MazeView;

/**
 * Controller for the Trivia Maze game. Handles game logic, player movement, answer submission,
 * save/load, and restart. Acts as the bridge between the model and view in the MVC pattern.
 *
 * @author peytonlaudanski, huyle
 * @version 5
 */
public class TriviaGame {

    /** Default maze size (width and height). */
    private static final int MAZE_SIZE = 4;

    /** The trivia maze model. */
    private TriviaMaze myMaze;

    /** The player model. */
    private Player myPlayer;

    /** The main game view. */
    private MazeView myView;

    /**
     * Constructs the controller with a maze and player.
     *
     * @param theMaze   the trivia maze
     * @param thePlayer the player
     */
    public TriviaGame(final TriviaMaze theMaze, final Player thePlayer) {
        setMaze(theMaze);
        setPlayer(thePlayer);
    }

    /**
     * Registers the view with this controller.
     *
     * @param theView the main game view
     */
    public void setView(final MazeView theView) {
        myView = theView;
    }

    /**
     * Handles a movement request in the given direction. If the door is locked, shows an
     * error. If open, moves immediately. If closed, prompts a question.
     *
     * @param theDir the requested direction
     */
    public void requestMove(final Direction theDir) {
        final TriviaRoom current = myMaze.getCurrentRoom();

        if (current.hasDoor(theDir)) {
            final TriviaDoor door = current.getDoor(theDir);
            if (door.getState() == DoorState.LOCKED) {
                myView.showMessage("This door is permanently locked!", Color.RED);
            } else if (door.getState() == DoorState.OPEN) {
                myMaze.attemptMove(theDir, true);
                postMoveUpdate();
            } else {
                myView.displayQuestion(door.getQuestion(), theDir);
            }
        }
    }

    /**
     * Processes the player's answer to a trivia question.
     *
     * @param theAnswer the submitted answer
     * @param theDir    the direction the player is trying to move
     */
    public void submitAnswer(final String theAnswer, final Direction theDir) {
        final TriviaRoom current = myMaze.getCurrentRoom();
        final TriviaDoor door = current.getDoor(theDir);
        final boolean correct = door.getQuestion().checkAnswer(theAnswer);

        if (correct) {
            myView.showMessage("\u2705 Correct! Door is now GREEN \u2014 move freely!",
                    Color.GREEN);
            myMaze.attemptMove(theDir, true);
        } else {
            myView.showMessage("\u274C Wrong! Door is now RED \u2014 locked forever.",
                    Color.RED);
            myMaze.attemptMove(theDir, false);
        }

        myView.clearQuestion();
        postMoveUpdate();
    }

    /**
     * Pushes updated state to the view after every move, then checks win/lose conditions.
     */
    private void postMoveUpdate() {
        myView.refreshView(myMaze.getRooms(), myPlayer.getX(), myPlayer.getY());

        if (myMaze.isExit()) {
            myView.showWin();
        } else if (!myMaze.canLeaveMaze()) {
            myView.showLose();
        }
    }

    /**
     * Saves the current game state to disk.
     */
    public void saveGame() {
        final GameState gameState = new GameState(getPlayer(), getMaze());
        final GameCaretaker caretaker = new GameCaretaker(gameState);
        caretaker.save();
    }

    /**
     * Loads a previously saved game state from disk and refreshes the view.
     */
    public void loadGame() {
        GameState gameState = new GameState(null, null);
        final GameCaretaker caretaker = new GameCaretaker(gameState);
        gameState = caretaker.load();

        setMaze(gameState.getMaze());
        setPlayer(gameState.getPlayer());

        if (myView != null) {
            pushMazeToView();
        }
    }

    /**
     * Resets the game with a freshly generated maze and notifies the view.
     */
    public void restartGame() {
        final Player player = new Player(0, 0);
        setMaze(buildNewMaze(player, MAZE_SIZE));
        setPlayer(player);

        if (myView != null) {
            pushMazeToView();
            myView.showMessage("\uD83D\uDD04 New game started!", Color.CYAN);
        }
    }

    /**
     * Ends the game session.
     */
    public void endGame() {
        System.exit(0);
    }

    /**
     * @return the current player
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * @return the current maze
     */
    public TriviaMaze getMaze() {
        return myMaze;
    }

    /**
     * @param theMaze the maze to set
     */
    private void setMaze(final TriviaMaze theMaze) {
        myMaze = theMaze;
    }

    /**
     * @param thePlayer the player to set
     */
    private void setPlayer(final Player thePlayer) {
        myPlayer = thePlayer;
    }

    /**
     * Sends the current rooms and player position to the view. Used on initial display, load,
     * and restart.
     */
    private void pushMazeToView() {
        myView.displayMaze(myMaze.getRooms(), myPlayer.getX(), myPlayer.getY());
    }

    /**
     * Builds a new maze with a freshly loaded question set.
     *
     * @param thePlayer the player to place in the maze
     * @param theSize   the width and height of the maze
     * @return a newly generated TriviaMaze
     */
    private static TriviaMaze buildNewMaze(final Player thePlayer, final int theSize) {
        new QuestionFactory();
        final MazeGenerator gen = new MazeGenerator(theSize, theSize);
        return gen.generateMaze(thePlayer, theSize - 1, theSize - 1);
    }

    /**
     * Builds the initial MVC stack and launches the Swing GUI on the EDT.
     */
    public static void launchGUI() {
        final Player player = new Player(0, 0);
        final TriviaMaze maze = buildNewMaze(player, MAZE_SIZE);

        SwingUtilities.invokeLater(() -> {
            final TriviaGame controller = new TriviaGame(maze, player);
            final MazeView view = new MazeView(controller);
            controller.setView(view);
            controller.pushMazeToView();
        });
    }
}