package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class TrueFalseQuestionFactory implements QuestionFactory {

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
    private final boolean myCorrectAnswer;

    /**
     * 
     * @param thePrompt
     * @param theID
     * @param theCorrectAnswer
     */
    public TrueFalseQuestionFactory(final String thePrompt, final int theID,
            final boolean theCorrectAnswer) {
        this.myPrompt = thePrompt;
        this.myID = theID;
        this.myCorrectAnswer = theCorrectAnswer;
    }

    /**
     * 
     */
    @Override
    public AbstractTriviaQuestion createQuestion() {
        return new TrueFalseQuestion(myPrompt, myID, myCorrectAnswer);
    }
}