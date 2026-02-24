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
    private final AbstractTriviaQuestion myQuestion;

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
     */
    public void lock() {
        myState = DoorState.LOCKED;

    }

    /**
     * 
     */
    public void open() {
        myState = DoorState.OPEN;

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
     * @return
     */
    public AbstractTriviaQuestion getQuestion() {
        return myQuestion;
    }

}
