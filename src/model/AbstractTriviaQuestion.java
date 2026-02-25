package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public abstract class AbstractTriviaQuestion {

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
