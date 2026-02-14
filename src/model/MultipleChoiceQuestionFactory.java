package model;

import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class MultipleChoiceQuestionFactory implements QuestionFactory {

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
    private final List<String> myChoices;

    /**
     * 
     */
    private final int myCorrectIndex;

    /**
     * 
     * @param thePrompt
     * @param theID
     * @param theChoices
     * @param theCorrectIndex
     */
    public MultipleChoiceQuestionFactory(final String thePrompt, final int theID,
            final List<String> theChoices, final int theCorrectIndex) {
        this.myPrompt = thePrompt;
        this.myID = theID;
        this.myChoices = theChoices;
        this.myCorrectIndex = theCorrectIndex;
    }

    /**
     * 
     */
    @Override
    public AbstractTriviaQuestion createQuestion() {
        return new MultipleChoiceQuestion(myPrompt, myID, myChoices, myCorrectIndex);
    }
}