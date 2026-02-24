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
    private AbstractTriviaQuestion myQuestion;

    /**
     * 
     */
    private DoorState myState;

    /**
     * 
     * @param theLocked
     */
    public TriviaDoor(final AbstractTriviaQuestion theQuestion, final DoorState theState) {
        myState = theState;
        myQuestion = theQuestion;
    }

    /**
     * 
     * @return
     */
    public DoorState getState() {
        return myState;
    }

    /**
     * 
     * @param theState
     */
    public void setState(final DoorState theState) {
        myState = theState;
    }

    /**
     * 
     * @return
     */
    public AbstractTriviaQuestion getQuestion() {
        return myQuestion;
    }

    /**
     * 
     * @param theQuestion
     */
    public void setQuestion(final AbstractTriviaQuestion theQuestion) {
        myQuestion = theQuestion;
    }

}
