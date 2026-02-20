package model;

import java.io.Serializable;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TriviaDoor implements Serializable {

    private static final long serialVersionUID = 1L;

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
