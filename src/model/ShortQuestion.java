package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class ShortQuestion extends AbstractTriviaQuestion {

    /**
     * 
     */
    private String myAnswer;

    /**
     * 
     * @param thePrompt
     * @param theID
     * @param theAnswer
     */
    public ShortQuestion(final String thePrompt, final int theID, final String theAnswer) {
        super(thePrompt, theID);
        setAnswer(theAnswer);
    }

    /**
     * 
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {

        Boolean bool = false;
        if (theAnswer != null) {
            bool = theAnswer.equals(myAnswer);
        }
        return bool;
    }

    /**
     * 
     * @return
     */
    public String getAnswer() {
        return myAnswer;
    }

    /**
     * 
     * @param theAnswer
     */
    private void setAnswer(final String theAnswer) {
        myAnswer = theAnswer;
    }

}
