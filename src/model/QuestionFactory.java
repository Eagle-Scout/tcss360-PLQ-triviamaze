package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sqlite.SQLiteDataSource;

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
    public QuestionFactory() {

        readQuestionsFromDatabase();
        shuffleQuestions();
        // intentionally empty to create questions with tester method, unsure if will remain
        // public when SQL database is populated and we start moving forward
    }

    /**
     * 
     * 
     * @param theType
     * @param theQuestion
     * @param theID
     * @param theData
     * @return
     */
    public AbstractTriviaQuestion createQuestion(final String theType,
            final String theQuestion, final int theID, final Object theData) {

        AbstractTriviaQuestion question = null;

        if ("MCQuestion".equals(theType)) {
            final MultipleChoiceData mcData = (MultipleChoiceData) theData;
            question = new MultipleChoiceQuestion(theQuestion, theID, mcData.getChoices(),
                    mcData.getCorrectIndex());

        } else if ("SQuestion".equals(theType)) {
            question = new ShortQuestion(theQuestion, theID, theData.toString());

        } else if ("TFQuestion".equals(theType)) {
            question = new TrueFalseQuestion(theQuestion, theID,
                    Boolean.parseBoolean(theData.toString()));
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
     * maybe pass into createQuestion of (QUESTION_TYPE, QUESTION, (question id), ANSWER).
     * 
     * in the case of MCQuestion maybe (QUESTION_TYPE, QUESTION, (question id), ("ANSWER",
     * MC_INDEX)).
     * 
     */
    static void readQuestionsFromDatabase() {
        SQLiteDataSource dataSource = null;

        try {
            dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:questions.db");
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Opened database successfully");

        final String query = "";

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();) {

            final int returnValue = stmt.executeUpdate(query);
            System.out.println("executeUpdate() returned " + returnValue);
        } catch (final SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("change query request");
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
        public MultipleChoiceData(final String theChoices, final int theCorrectIndex) {
            final String[] array = theChoices.split(", ");
            myChoices = Arrays.asList(array);
            myCorrectIndex = theCorrectIndex;
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