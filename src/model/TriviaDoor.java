package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TriviaDoor {

    /**
     * 
     */
    private boolean myLocked;

    /**
     * 
     */
    private final AbstractTriviaQuestion myQuestion;

    /**
     * 
     * @param theLocked
     */
    public TriviaDoor(final boolean theLocked, final AbstractTriviaQuestion theQuestion) {

        myQuestion = theQuestion;
        myLocked = theLocked;
    }


    /**
     * 
     */
    public void lock() {
        myLocked = true;
    }

    /**
     * 
     * @return
     */
    public boolean isLocked() {
        return myLocked;
    }

    /**
     * 
     * @return
     */
    public AbstractTriviaQuestion getQuestion() {
        return myQuestion;
    }

}
