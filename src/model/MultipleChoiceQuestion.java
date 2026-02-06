package model;

import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public class MultipleChoiceQuestion extends AbstractQuestion {

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
     * @param thePrompt
     * @param theID
     * @param theChoices
     * @param theIndex
     */
    public MultipleChoiceQuestion(final String thePrompt, final int theID,
            final List<String> theChoices, final int theIndex) {
        super(thePrompt, theID);
        setChoices(theChoices);
        setIndex(theIndex);
    }

    /**
     * 
     */
    @Override
    public boolean checkAnswer(final String theAnswer) {

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
