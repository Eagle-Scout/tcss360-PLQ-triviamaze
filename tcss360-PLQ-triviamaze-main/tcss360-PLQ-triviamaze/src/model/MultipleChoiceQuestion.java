package model;

import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class MultipleChoiceQuestion extends AbstractTriviaQuestion {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private List<String> myChoices;

    /**
     * 
     */
    private int myIndex;

    /**
     * 
     * @param theQuestion
     * @param theID
     * @param theChoices
     * @param theIndex
     */
    public MultipleChoiceQuestion(final String theQuestion, final int theID,
            final List<String> theChoices, final int theIndex) {
        super(theQuestion, theID);
        setChoices(theChoices);
        setIndex(theIndex);
    }

    /**
     * 
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {

        if (myIndex < 0 || myIndex >= myChoices.size()) {
            throw new IllegalStateException("Invalid question: index out of bounds");
        }
        return theAnswer.equals(myChoices.get(myIndex));
    }

    /**
     * 
     * @param theChoices
     */
    private void setChoices(final List<String> theChoices) {
        myChoices = theChoices;
    }

    /**
     * 
     * @param theIndex
     */
    private void setIndex(final int theIndex) {
        myIndex = theIndex;
    }

    /**
     * 
     * @return
     */
    public List<String> getChoices() {
        return myChoices;
    }

    /**
     * 
     * @return
     */
    public int getIndex() {
        return myIndex;
    }

}
