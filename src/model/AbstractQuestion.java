package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public abstract class AbstractQuestion {

    /**
     * 
     */
    private int myID;

    /**
     * 
     */
    private String myPrompt;

    /**
     * 
     * @param thePrompt
     * @param theID
     */
    public AbstractQuestion(final String thePrompt, final int theID) {
        setPrompt(thePrompt);
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
     * @param thePrompt
     */
    private void setPrompt(final String thePrompt) {
        this.myPrompt = thePrompt;
    }

    /**
     * 
     * @param theID
     */
    private void setID(final int theID) {
        this.myID = theID;
    }

    /**
     * 
     * @return
     */
    public String getPrompt() {
        return myPrompt;
    }

    /**
     * 
     * @return
     */
    public int getID() {
        return myID;
    }

}
