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
        // testMain();

    }

    // ai generated test method, uncomment method to test pretty much everything. not
    // completely up to date, since it doesnt take into account triviamaze/attemptmove

    // public static void testMain() {
    //
    // System.out.println("=== TRIVIA MAZE TEST ===\n");
    //
    // // ============ CREATE QUESTIONS ============
    //
    // final QuestionFactory q1Factory =
    // new ShortQuestionFactory("What is the capital of France?", 1, "Paris");
    // final AbstractTriviaQuestion q1 = q1Factory.createQuestion();
    //
    // final QuestionFactory q2Factory =
    // new TrueFalseQuestionFactory("The Earth is flat", 2, false);
    // final AbstractTriviaQuestion q2 = q2Factory.createQuestion();
    //
    // final QuestionFactory q3Factory = new MultipleChoiceQuestionFactory("What is 2 + 2?",
    // 3, java.util.Arrays.asList("3", "4", "5"), 1);
    // final AbstractTriviaQuestion q3 = q3Factory.createQuestion();
    //
    // final QuestionFactory q4Factory =
    // new ShortQuestionFactory("What is the largest planet?", 4, "Jupiter");
    // final AbstractTriviaQuestion q4 = q4Factory.createQuestion();
    //
    // // ============ CREATE ROOMS ============
    // // Coordinate system: X increases EAST, Y increases SOUTH
    // // Arrays: rooms[Y][X] where Y=0 is TOP, Y=1 is BOTTOM
    //
    // // Room (0, 1) - Bottom-left
    // final java.util.Map<Direction, TriviaDoor> room01Doors = new java.util.HashMap<>();
    // room01Doors.put(Direction.NORTH, new TriviaDoor(false, q1)); // Door to (0, 0)
    // room01Doors.put(Direction.EAST, new TriviaDoor(false, q4)); // Door to (1, 1)
    // final TriviaRoom room01 = new TriviaRoom(room01Doors);
    //
    // // Room (1, 1) - Bottom-right
    // final java.util.Map<Direction, TriviaDoor> room11Doors = new java.util.HashMap<>();
    // room11Doors.put(Direction.WEST, new TriviaDoor(false, q4)); // Door to (0, 1)
    // room11Doors.put(Direction.NORTH, new TriviaDoor(false, q3)); // Door to (1, 0)
    // final TriviaRoom room11 = new TriviaRoom(room11Doors);
    //
    // // Room (0, 0) - Top-left (START)
    // final java.util.Map<Direction, TriviaDoor> room00Doors = new java.util.HashMap<>();
    // room00Doors.put(Direction.SOUTH, new TriviaDoor(false, q1)); // Door to (0, 1)
    // room00Doors.put(Direction.EAST, new TriviaDoor(false, q2)); // Door to (1, 0)
    // final TriviaRoom room00 = new TriviaRoom(room00Doors);
    //
    // // Room (1, 0) - Top-right
    // final java.util.Map<Direction, TriviaDoor> room10Doors = new java.util.HashMap<>();
    // room10Doors.put(Direction.WEST, new TriviaDoor(false, q2)); // Door to (0, 0)
    // room10Doors.put(Direction.SOUTH, new TriviaDoor(false, q3)); // Door to (1, 1)
    // final TriviaRoom room10 = new TriviaRoom(room10Doors);
    //
    // // ============ CREATE MAZE ============
    // // rows[Y][X] where Y=0 is top, Y=1 is bottom
    //
    // final TriviaRoom[][] rooms = {{room00, room10}, // Y = 0 (TOP)
    // {room01, room11} // Y = 1 (BOTTOM)
    // };
    //
    // Player player = new Player(0, 0); // Start top-left
    // TriviaMaze maze = new TriviaMaze(rooms, player);
    //
    // // ============ TEST 1: Initial State ============
    //
    // System.out.println(
    // "Initial player position: (" + player.getX() + ", " + player.getY() + ")");
    // System.out.println(
    // "Current room has " + maze.getCurrentRoom().getDoors().size() + " doors\n");
    //
    // // ============ TEST 2: Move SOUTH (down) ============
    //
    // System.out.println("--- Test: Move SOUTH (down) ---");
    // System.out.println("Before: (" + player.getX() + ", " + player.getY() + ")");
    //
    // final TriviaRoom current1 = maze.getCurrentRoom();
    // if (current1.hasDoor(Direction.SOUTH)) {
    // final AbstractTriviaQuestion question =
    // current1.getDoor(Direction.SOUTH).getQuestion();
    // System.out.println("Q: " + question.getPrompt());
    // final boolean correct = question.checkAnswer("Paris");
    // System.out.println("A: Paris - " + (correct ? "✓ CORRECT" : "✗ WRONG"));
    // }
    //
    // maze.movePlayer(Direction.SOUTH);
    // System.out.println("After: (" + player.getX() + ", " + player.getY() + ")");
    // System.out.println("Expected: (0, 1) - "
    // + (player.getX() == 0 && player.getY() == 1 ? "✓" : "✗") + "\n");
    //
    // // ============ TEST 3: Move EAST (right) ============
    //
    // System.out.println("--- Test: Move EAST (right) ---");
    // System.out.println("Before: (" + player.getX() + ", " + player.getY() + ")");
    //
    // final TriviaRoom current2 = maze.getCurrentRoom();
    // if (current2.hasDoor(Direction.EAST)) {
    // final AbstractTriviaQuestion question =
    // current2.getDoor(Direction.EAST).getQuestion();
    // System.out.println("Q: " + question.getPrompt());
    // final boolean correct = question.checkAnswer("Jupiter");
    // System.out.println("A: Jupiter - " + (correct ? "✓ CORRECT" : "✗ WRONG"));
    // }
    //
    // maze.movePlayer(Direction.EAST);
    // System.out.println("After: (" + player.getX() + ", " + player.getY() + ")");
    // System.out.println("Expected: (1, 1) - "
    // + (player.getX() == 1 && player.getY() == 1 ? "✓" : "✗") + "\n");
    //
    // // ============ TEST 4: Move NORTH (up) ============
    //
    // System.out.println("--- Test: Move NORTH (up) ---");
    // System.out.println("Before: (" + player.getX() + ", " + player.getY() + ")");
    //
    // final TriviaRoom current3 = maze.getCurrentRoom();
    // if (current3.hasDoor(Direction.NORTH)) {
    // final AbstractTriviaQuestion question =
    // current3.getDoor(Direction.NORTH).getQuestion();
    // System.out.println("Q: " + question.getPrompt());
    // final boolean correct = question.checkAnswer("4");
    // System.out.println("A: 4 - " + (correct ? "✓ CORRECT" : "✗ WRONG"));
    // }
    //
    // maze.movePlayer(Direction.NORTH);
    // System.out.println("After: (" + player.getX() + ", " + player.getY() + ")");
    // System.out.println("Expected: (1, 0) - "
    // + (player.getX() == 1 && player.getY() == 0 ? "✓" : "✗") + "\n");
    //
    // // ============ TEST 5: Try Invalid Move (no door) ============
    //
    // System.out.println("--- Test: Invalid Move (no EAST door) ---");
    // System.out.println("Before: (" + player.getX() + ", " + player.getY() + ")");
    //
    // final int posBefore = player.getX();
    // maze.movePlayer(Direction.EAST);
    // final int posAfter = player.getX();
    //
    // System.out.println("After: (" + player.getX() + ", " + player.getY() + ")");
    // System.out
    // .println("Position unchanged: " + (posBefore == posAfter ? "✓" : "✗") + "\n");
    //
    // // ============ TEST 6: QuestionFactorySelector ============
    //
    // System.out.println("--- Test: QuestionFactorySelector ---");
    // final QuestionFactory selectorFactory = QuestionFactorySelector.createFactory(
    // QuestionFactorySelector.QuestionType.MULTIPLE_CHOICE,
    // "What year was Java created?", 99,
    // new QuestionFactorySelector.MultipleChoiceData(
    // java.util.Arrays.asList("1991", "1995", "2001"), 1));
    // final AbstractTriviaQuestion selectorQuestion = selectorFactory.createQuestion();
    // System.out.println("Q: " + selectorQuestion.getPrompt());
    // System.out.println("A: 1995 - "
    // + (selectorQuestion.checkAnswer("1995") ? "✓ CORRECT" : "✗ WRONG") + "\n");
    //
    // // --- Test: Move is BLOCKED if door is locked after wrong answer ---
    //
    // // Assume player is at (0, 0), room has SOUTH door with q1 ("What is the capital of
    // // France?", answer: "Paris").
    // // We'll simulate locking the SOUTH door after a wrong answer.
    //
    // player = new Player(0, 0);
    // maze = new TriviaMaze(rooms, player);
    //
    // // Simulate wrong answer (e.g. "Rome") so we lock the door:
    //
    // final TriviaRoom current = maze.getCurrentRoom();
    // if (current.hasDoor(Direction.SOUTH)) {
    // final AbstractTriviaQuestion question =
    // current.getDoor(Direction.SOUTH).getQuestion();
    // System.out.println("Q: " + question.getPrompt());
    // final boolean correct = question.checkAnswer("Rome");
    // System.out.println("A: Paris - " + (correct ? "✓ CORRECT" : "✗ WRONG"));
    // }
    //
    // System.out.println("Trying to move SOUTH through the locked door...");
    // final int oldY = player.getY();
    // maze.movePlayer(Direction.SOUTH);
    // final int newY = player.getY();
    //
    // if (oldY == newY) {
    // System.out.println("Move was BLOCKED (as expected).");
    // } else {
    // System.out.println("ERROR: Move succeeded despite locked door!");
    // }
    //
    // System.out.println("=== ALL TESTS COMPLETE ===");
    // }
}