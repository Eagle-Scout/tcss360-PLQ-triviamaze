package control;

import java.awt.Color;

import javax.swing.SwingUtilities;

import model.Direction;
import model.DoorState;
import model.MazeGenerator;
import model.Player;
import model.QuestionFactory;
import model.TriviaDoor;
import model.TriviaMaze;
import model.TriviaRoom;
import view.MazeView;

/**
 *
 * @author peytonlaudanski, huyle
 * @version 4
 */
public class MazeController {

    /**
     * 
     */
    private TriviaMaze myMaze;

    /**
     * 
     */
    private Player myPlayer;

    /**
     * 
     */
    private MazeView myView;

    /**
     * 
     * @param theMaze
     * @param thePlayer
     */
    public MazeController(final TriviaMaze theMaze, final Player thePlayer) {
        setMaze(theMaze);
        setPlayer(thePlayer);
    }

    /* ================= VIEW REGISTRATION ================= */

    /**
     * 
     * @param theView
     */
    public void setView(final MazeView theView) {
        myView = theView;
    }

    /* ================= SAVE / LOAD ================= */

    /**
     * 
     */
    public void saveGame() {
        final GameState gameState = new GameState(getPlayer(), getMaze());
        final GameCaretaker caretaker = new GameCaretaker(gameState);
        caretaker.save();
    }

    /**
     * 
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

    /* ================= CORE GAME LOGIC ================= */

    /**
     * 
     * @param theDir
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
     * 
     * @param theAnswer
     * @param theDir
     */
    public void submitAnswer(final String theAnswer, final Direction theDir) {
        final TriviaRoom current = myMaze.getCurrentRoom();
        final TriviaDoor door = current.getDoor(theDir);

        final boolean correct = door.getQuestion().checkAnswer(theAnswer);

        if (correct) {
            myView.showMessage("✅ Correct! Door is now GREEN — move freely!", Color.GREEN);
            myMaze.attemptMove(theDir, true);
        } else {
            myView.showMessage("❌ Wrong! Door is now RED — locked forever.", Color.RED);
            myMaze.attemptMove(theDir, false);
        }

        myView.clearQuestion();
        postMoveUpdate();
    }

    /**
     * Pushes updated rooms and exact player position to the view after every move.
     */
    private void postMoveUpdate() {
        myView.refreshView(myMaze.getRooms(), myPlayer.getX(), myPlayer.getY());

        if (myMaze.isExit()) {
            myView.showWin();
            return;
        }

        if (isPlayerTrapped()) {
            myView.showLose();
        }
    }

    /**
     * 
     * @return
     */
    private boolean isPlayerTrapped() {
        return !myMaze.canLeaveMaze();
    }

    /**
     * Sends rooms and current player position to the view. Used for initial display, load, and
     * restart — always uses real player coords.
     */
    private void pushMazeToView() {
        myView.displayMaze(myMaze.getRooms(), myPlayer.getX(), myPlayer.getY());
    }

    /* ================= RESTART ================= */

    /**
     * 
     */
    public void restartGame() {
        new QuestionFactory();

        final int size = 6;
        final Player player = new Player(0, 0);
        final MazeGenerator gen = new MazeGenerator(size, size);
        final TriviaMaze maze = gen.generateMaze(player, size - 1, size - 1);

        setMaze(maze);
        setPlayer(player);

        if (myView != null) {
            pushMazeToView();
            myView.showMessage("🔄 New game started!", Color.CYAN);
        }
    }

    /**
     * 
     */
    public void endGame() {
        System.out.println("just hit alt + f4 atp its not ready yet");
    }

    /* ================= GETTERS / SETTERS ================= */

    /**
     * 
     * @param theMaze
     */
    private void setMaze(final TriviaMaze theMaze) {
        myMaze = theMaze;
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
     * @return
     */
    public TriviaMaze getMaze() {
        return myMaze;
    }

    /* ================= LAUNCH ================= */

    /**
     * 
     */
    public static void launchGUI() {
        new QuestionFactory();

        final int size = 5;
        final Player player = new Player(0, 0);
        final MazeGenerator gen = new MazeGenerator(size, size);
        final TriviaMaze maze = gen.generateMaze(player, size - 1, size - 1);

        SwingUtilities.invokeLater(() -> {
            final MazeController controller = new MazeController(maze, player);
            final MazeView view = new MazeView(controller);
            controller.setView(view);
            controller.pushMazeToView();
        });
    }

    /**
     * 
     * @param theArgs
     */
    public static void main(final String[] theArgs) {
        launchGUI();
    }

}