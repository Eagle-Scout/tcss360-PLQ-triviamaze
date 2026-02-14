package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TrueFalseQuestion extends AbstractTriviaQuestion {

    /**
     * 
     */
    private boolean myCorrectAnswer;

    /**
     * 
     * @param thePrompt
     * @param theID
     * @param theBool
     */
    public TrueFalseQuestion(final String thePrompt, final int theID, final boolean theBool) {
        super(thePrompt, theID);
        setCorrect(theBool);
    }

    /**
     * 
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {
        return theAnswer.equals(String.valueOf(myCorrectAnswer));
    }

    /**
     * 
     * @return
     */
    public boolean isCorrect() {
        return myCorrectAnswer;
    }

    /**
     * 
     * @param theBool
     */
    private void setCorrect(final boolean theBool) {
        myCorrectAnswer = theBool;
    }

}
