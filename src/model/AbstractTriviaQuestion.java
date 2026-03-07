package model;

import java.io.Serializable;

/**
 * Fixed: Added implements Serializable and serialVersionUID, so subclasses can be saved/loaded
 * via GameCaretaker (ObjectOutputStream).
 * 
 * @author peytonlaudanski, huyle
 * @version 2
 */
public abstract class AbstractTriviaQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private int myID;

    /**
     * 
     */
    private String myQuestion;

    /**
     * 
     * @param theQuestion
     * @param theID
     */
    public AbstractTriviaQuestion(final String theQuestion, final int theID) {
        setQuestion(theQuestion);
        setID(theID);
    }

    /**
     * 
     * @param theAnswer
     * @return
     */
    public abstract boolean checkAnswer(String theAnswer);

    /**
     * 
     * @param theQuestion
     */
    private void setQuestion(final String theQuestion) {
        myQuestion = theQuestion;
    }

    /**
     * 
     * @param theID
     */
    private void setID(final int theID) {
        myID = theID;
    }

    /**
     * 
     * @return
     */
    public String getQuestion() {
        return myQuestion;
    }

    /**
     * 
     * @return
     */
    public int getID() {
        return myID;
    }

}
