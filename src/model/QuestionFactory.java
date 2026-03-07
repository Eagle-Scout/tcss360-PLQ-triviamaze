package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sqlite.SQLiteDataSource;

/**
 * 
 * @author peytonlaudanski, huyle
 * @version 2
 */
public final class QuestionFactory {

    /**
     * 
     */
    private static List<AbstractTriviaQuestion> myQuestions = new ArrayList<>();

    /**
     * Fixed: Starts at -1 so first call to getQuestion() returns index 0.
     */
    private static int myQuestionIndex = -1;

    /**
     * 
     */
    public QuestionFactory() {
        myQuestions.clear();
        readQuestionsFromDatabase();
        shuffleQuestions();
    }

    /**
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
     * Fixed: This was doing myQuestionID++ then get(myQuestionID) which skipped index 0 and
     * crashed on empty list. Now cycles safely through all questions.
     *
     * @return next question from the list
     */
    public static AbstractTriviaQuestion getQuestion() {
        if (myQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No questions loaded! Run PopulateDatabase.main() first.");
        }
        myQuestionIndex = (myQuestionIndex + 1) % myQuestions.size();
        return myQuestions.get(myQuestionIndex);
    }

    /**
     * Fixed: This was using empty query "" with executeUpdate() which loaded nothing. Now uses
     * SELECT * FROM questions with executeQuery() and reads TYPE column to build the correct
     * question subclass.
     */
    static void readQuestionsFromDatabase() {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:questions.db");

        System.out.println("Opened database successfully");

        final String query = "SELECT * FROM questions";

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            int questionID = 1;
            while (resultSet.next()) {
                final String type = resultSet.getString("TYPE");
                final String question = resultSet.getString("QUESTION");
                final String answer = resultSet.getString("ANSWER");
                final String choices = resultSet.getString("CHOICES");

                switch (type) {
                    case "SHORT" ->
                        myQuestions.add(new ShortQuestion(question, questionID, answer));

                    case "TF" -> myQuestions.add(new TrueFalseQuestion(question, questionID,
                            Boolean.parseBoolean(answer)));

                    case "MC" -> {
                        final List<String> choiceList = Arrays.asList(choices.split("\\|"));
                        myQuestions.add(new MultipleChoiceQuestion(question, questionID,
                                choiceList, choiceList.indexOf(answer)));
                    }

                    default -> System.err.println("Unknown question type: " + type);
                }
                questionID++;
            }

            System.out.println("Loaded " + myQuestions.size() + " questions from database");

        } catch (final SQLException e) {
            Logger.getLogger(QuestionFactory.class.getName()).log(Level.SEVERE, "", e);
        }
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