package model;

/**
 * Represents a door connecting rooms in the trivia maze.
 * A door may be locked and guarded by a trivia question.
 *
 * Basic behavior:
 * - If locked, the player must answer correctly to unlock it.
 * - Supports limited attempts (default 3).
 *
 * @author peytonlaudanski,quinnweinzoff
 * @version 2
 */
public class TriviaDoor {

    /** True if the door is currently locked. */
    private boolean myLocked;

    /** The question guarding this door (can be null in early prototypes). */
    private final AbstractTriviaQuestion myQuestion;

    /** Max attempts allowed to unlock the door. */
    private final int myMaxAttempts;

    /** Attempts used so far while trying to unlock. */
    private int myAttemptsUsed;

    /**
     * Constructs a door with a locked state and a guarding question.
     * Defaults max attempts to 3.
     *
     * @param theLocked initial locked state
     * @param theQuestion guarding question (may be null)
     */
    public TriviaDoor(final boolean theLocked, final AbstractTriviaQuestion theQuestion) {
        this(theLocked, theQuestion, 3);
    }

    /**
     * Constructs a door with a locked state, guarding question, and max attempts.
     *
     * @param theLocked initial locked state
     * @param theQuestion guarding question (may be null)
     * @param theMaxAttempts max attempts (minimum 1)
     */
    public TriviaDoor(final boolean theLocked, final AbstractTriviaQuestion theQuestion,
                      final int theMaxAttempts) {

        myQuestion = theQuestion;
        myLocked = theLocked;

        myMaxAttempts = Math.max(1, theMaxAttempts);
        myAttemptsUsed = 0;
    }

    /** Locks the door. */
    public void lock() {
        myLocked = true;
    }

    /** Unlocks the door. */
    public void unlock() {
        myLocked = false;
    }

    /**
     * Keeps your friend’s original method name so nothing else breaks.
     *
     * @return true if locked
     */
    public boolean isMyLocked() {
        return myLocked;
    }

    /**
     * @return the guarding question (may be null)
     */
    public AbstractTriviaQuestion getQuestion() {
        return myQuestion;
    }

    /**
     * Attempt to unlock the door using an answer.
     *
     * Rules:
     * - If already unlocked, returns true.
     * - If out of attempts, returns false.
     * - If question is null or answer is null, returns false.
     * - If correct, unlocks door and returns true.
     * - If incorrect, consumes an attempt and returns false.
     *
     * @param theAnswer the player's answer
     * @return true if the door becomes (or already is) unlocked
     */
    public boolean attemptUnlock(final String theAnswer) {
        if (!myLocked) {
            return true;
        }

        if (myAttemptsUsed >= myMaxAttempts) {
            return false;
        }

        if (myQuestion == null || theAnswer == null) {
            return false;
        }

        final boolean correct = myQuestion.checkAnswer(theAnswer);

        if (correct) {
            unlock();
            return true;
        } else {
            myAttemptsUsed++;
            return false;
        }
    }

    /**
     * @return number of attempts remaining before the door is stuck locked
     */
    public int getAttemptsRemaining() {
        return Math.max(0, myMaxAttempts - myAttemptsUsed);
    }

    /** @return attempts used so far */
    public int getAttemptsUsed() {
        return myAttemptsUsed;
    }

    /** @return maximum attempts allowed */
    public int getMaxAttempts() {
        return myMaxAttempts;
    }
}
