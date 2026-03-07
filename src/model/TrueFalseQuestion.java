package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TrueFalseQuestion extends AbstractTriviaQuestion {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private boolean myAnswer;

    /**
     * 
     * @param theQuestion
     * @param theID
     * @param theBool
     */
    public TrueFalseQuestion(final String theQuestion, final int theID,
            final boolean theBool) {
        super(theQuestion, theID);
        setCorrect(theBool);
    }

    /**
     * 
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {

        return theAnswer.equals(String.valueOf(myAnswer));
    }

    /**
     * 
     * @return
     */
    public boolean isCorrect() {
        return myAnswer;
    }

    /**
     * 
     * @param theAnswer
     */
    private void setCorrect(final boolean theAnswer) {
        myAnswer = theAnswer;
    }

}
