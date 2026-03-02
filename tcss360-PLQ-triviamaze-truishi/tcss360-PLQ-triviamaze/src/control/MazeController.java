package control;

import java.util.HashMap;

import model.AbstractTriviaQuestion;
import model.Direction;
import model.DoorState;
import model.Player;
import model.QuestionFactory;
import model.TriviaDoor;
import model.TriviaMaze;
import model.TriviaRoom;

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
    private Player myPlayer;

    /**
     * 
     * @param theMaze
     * @param theView
     */
    public MazeController(final TriviaMaze theMaze, final Player thePlayer) {
        setMaze(theMaze);
        setPlayer(thePlayer);
    }

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

    /**
     * 
     * @param theArgs
     */
    public static void main(final String[] theArgs) {

        // final MazeView view = new MazeView();

        final QuestionFactory factory = new QuestionFactory();

        // personal check for if mcquestion logic is correct

        final AbstractTriviaQuestion q3 = factory.createQuestion("MCQuestion",
                "What is 2 + 2?", 3, new QuestionFactory.MultipleChoiceData("3, 4, 5", 1));
        System.out.println(q3.checkAnswer("4"));

        // testMain();

    }

    /**
     * ai generated test method, uncomment method to test pretty much everything.
     */
    public static void testMain() {
        System.out.println("=== TRIVIA MAZE TEST (UPDATED) ===\n");

        // ========= CREATE QUESTIONS =========
        final QuestionFactory factory = new QuestionFactory();

        final AbstractTriviaQuestion q1 = factory.createQuestion("SQuestion",
                "What is the capital of France?", 1, "Paris");

        final AbstractTriviaQuestion q2 =
                factory.createQuestion("TFQuestion", "The Earth is flat", 2, false);

        final AbstractTriviaQuestion q3 = factory.createQuestion("MCQuestion",
                "What is 2 + 2?", 3, new QuestionFactory.MultipleChoiceData("3, 4, 5", 1));

        // ========= CREATE ROOMS =========

        // Bottom-left (0,1)
        final var room01Doors = new HashMap<Direction, TriviaDoor>();
        room01Doors.put(Direction.NORTH, new TriviaDoor(q1, DoorState.CLOSED));
        room01Doors.put(Direction.EAST, new TriviaDoor(q3, DoorState.CLOSED));
        final TriviaRoom room01 = new TriviaRoom(room01Doors);

        // Bottom-right (1,1)
        final var room11Doors = new HashMap<Direction, TriviaDoor>();
        room11Doors.put(Direction.WEST, new TriviaDoor(q3, DoorState.CLOSED));
        room11Doors.put(Direction.NORTH, new TriviaDoor(q2, DoorState.CLOSED));
        final TriviaRoom room11 = new TriviaRoom(room11Doors);

        // Top-left (0,0)
        final var room00Doors = new HashMap<Direction, TriviaDoor>();
        room00Doors.put(Direction.SOUTH, new TriviaDoor(q1, DoorState.CLOSED));
        room00Doors.put(Direction.EAST, new TriviaDoor(q2, DoorState.CLOSED));
        final TriviaRoom room00 = new TriviaRoom(room00Doors);

        // Top-right (1,0)
        final var room10Doors = new HashMap<Direction, TriviaDoor>();
        room10Doors.put(Direction.WEST, new TriviaDoor(q2, DoorState.CLOSED));
        room10Doors.put(Direction.SOUTH, new TriviaDoor(q3, DoorState.CLOSED));
        final TriviaRoom room10 = new TriviaRoom(room10Doors);

        final TriviaRoom[][] rooms = {{room00, room10}, {room01, room11}};

        Player player = new Player(0, 0);
        TriviaMaze maze = new TriviaMaze(rooms, player, 1, 1);

        // ========= TEST 1: Correct answer allows movement =========
        System.out.println("--- Test: Correct answer allows movement ---");

        final AbstractTriviaQuestion q = maze.getCurrentRoom().getQuestion(Direction.SOUTH);

        final boolean correct = q.checkAnswer("Paris");
        maze.attemptMove(Direction.SOUTH, correct);

        System.out.println("Player position: (" + player.getX() + ", " + player.getY() + ")");

        System.out.println(player.getY() == 1 ? "✓ PASS\n" : "✗ FAIL\n");

        // ========= TEST 2: Wrong answer locks door =========
        System.out.println("--- Test: Wrong answer locks door ---");

        player = new Player(0, 0);
        maze = new TriviaMaze(rooms, player, 1, 1);

        final AbstractTriviaQuestion qWrong =
                maze.getCurrentRoom().getQuestion(Direction.SOUTH);

        final boolean wrong = qWrong.checkAnswer("Rome");

        maze.attemptMove(Direction.SOUTH, wrong);

        final TriviaDoor door = maze.getCurrentRoom().getDoor(Direction.SOUTH);

        System.out.println(door.getState() == DoorState.LOCKED ? "✓ Door locked\n"
                : "✗ Door NOT locked\n");

        System.out.println("=== TESTS COMPLETE ===");
    }

}