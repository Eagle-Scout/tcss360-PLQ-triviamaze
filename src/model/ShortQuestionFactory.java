package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class ShortQuestionFactory implements QuestionFactory {

    /**
     * 
     */
    private final String myPrompt;

    /**
     * 
     */
    private final int myID;

    /**
     * 
     */
    private final String myAnswer;

    /**
     * 
     * @param thePrompt
     * @param theID
     * @param theAnswer
     */
    public ShortQuestionFactory(final String thePrompt, final int theID,
            final String theAnswer) {
        this.myPrompt = thePrompt;
        this.myID = theID;
        this.myAnswer = theAnswer;
    }

    /**
     * 
     */
    @Override
    public AbstractTriviaQuestion createQuestion() {
        return new ShortQuestion(myPrompt, myID, myAnswer);
    }
}