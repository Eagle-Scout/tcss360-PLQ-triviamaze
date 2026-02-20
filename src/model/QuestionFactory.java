package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public final class QuestionFactory {

    /**
     * 
     */
    private static List<AbstractTriviaQuestion> myQuestions = new ArrayList<>();

    /**
     * 
     */
    private static int myQuestionID;

    /**
     * 
     */
    private QuestionFactory() {
    }

    /**
     * 
     * @param theType
     * @param thePrompt
     * @param theID
     * @param theData
     * @return
     */
    public AbstractTriviaQuestion createQuestion(final String theType, final String thePrompt,
            final int theID, final Object theData) {

        AbstractTriviaQuestion question = null;

        if ("MCQuestion".equals(theType)) {

            final MultipleChoiceData mcData = (MultipleChoiceData) theData;
            question = new MultipleChoiceQuestion(thePrompt, theID, mcData.getChoices(),
                    mcData.getCorrectIndex());
        } else if ("SQuestion".equals(theType)) {
            question = new ShortQuestion(thePrompt, theID, (String) theData);
        } else if ("TFQuestion".equals(theType)) {
            question = new TrueFalseQuestion(thePrompt, theID, (boolean) theData);
        }

        return question;
    }

    /**
     * 
     * @return
     */
    public static AbstractTriviaQuestion getQuestion() {
        myQuestionID++;

        return myQuestions.get(myQuestionID);
    }

    /**
     * 
     */
    static void readQuestionsFromDatabase() {

        // establish database connection
        // while there are questions in the question table
        // read a question
        // create a Question object
        // add that Question object to the ArrayList of Question

    }

    /**
     * 
     */
    static void shuffleQuestions() {
        Collections.shuffle(myQuestions);

    }

    /**
     * 
     */
    public static final class MultipleChoiceData {

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
         * @param theChoices
         * @param theCorrectIndex
         */
        public MultipleChoiceData(final List<String> theChoices, final int theCorrectIndex) {
            this.myChoices = theChoices;
            this.myCorrectIndex = theCorrectIndex;
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
        public int getCorrectIndex() {
            return myCorrectIndex;
        }
    }
}