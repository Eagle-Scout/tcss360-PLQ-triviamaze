package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sqlite.SQLiteDataSource;

/**
 * QuestionFactory
 *
 * Loads trivia questions from the SQLite database and provides them to the
 * rest of the program.
 *
 * Database file: questions.db
 * Expected table schema:
 *   questions(QUESTION_TYPE TEXT, QUESTION TEXT, ANSWER TEXT, MC_INDEX INTEGER)
 *
 * Conventions used in this project:
 * - QUESTION_TYPE is one of: "SQuestion", "TFQuestion", "MCQuestion"
 * - For SQuestion: ANSWER is the short answer string
 * - For TFQuestion: ANSWER is "true" or "false" (case-insensitive)
 * - For MCQuestion: ANSWER is the comma+space delimited choices list,
 *   and MC_INDEX is the correct choice index (1-based or 0-based).
 *
 * @author truishi
 * @author peytonlaudanski
 * @version 2
 */
public final class QuestionFactory {

    /** Default SQLite URL (relative to the working directory). */
    private static final String DB_URL = "jdbc:sqlite:questions.db";

    /** Loaded questions, shuffled for random gameplay. */
    private static final List<AbstractTriviaQuestion> QUESTIONS = new ArrayList<>();

    /** Next index to serve from {@link #QUESTIONS}. */
    private static int myNextIndex;

    /** Whether questions have already been loaded for this run. */
    private static boolean myLoaded;

    /**
     * Constructs a QuestionFactory and ensures the question list is loaded.
     */
    public QuestionFactory() {
        ensureLoaded();
    }

    /**
     * Creates a question object from provided data.
     *
     * @param theType question type string (SQuestion, TFQuestion, MCQuestion)
     * @param theQuestion question prompt
     * @param theID id to assign
     * @param theData answer data (String for S/TF, MultipleChoiceData for MC)
     * @return the created question, or null if theType is unknown
     */
    public static AbstractTriviaQuestion createQuestion(final String theType,
            final String theQuestion, final int theID, final Object theData) {

        AbstractTriviaQuestion question = null;

        if ("MCQuestion".equals(theType)) {
            final MultipleChoiceData mcData = (MultipleChoiceData) theData;
            question = new MultipleChoiceQuestion(theQuestion, theID, mcData.getChoices(),
                    mcData.getCorrectIndex());

        } else if ("SQuestion".equals(theType)) {
            question = new ShortQuestion(theQuestion, theID, String.valueOf(theData));

        } else if ("TFQuestion".equals(theType)) {
            question = new TrueFalseQuestion(theQuestion, theID,
                    Boolean.parseBoolean(String.valueOf(theData)));
        }

        return question;
    }

    /**
     * Returns the next question from the shuffled list.
     * Cycles back to the start when the end is reached.
     *
     * @return next question
     * @throws IllegalStateException if the database did not provide any questions
     */
    public static AbstractTriviaQuestion getQuestion() {
        ensureLoaded();

        if (QUESTIONS.isEmpty()) {
            throw new IllegalStateException("No questions loaded from questions.db");
        }

        if (myNextIndex >= QUESTIONS.size()) {
            myNextIndex = 0;
            Collections.shuffle(QUESTIONS);
        }

        return QUESTIONS.get(myNextIndex++);
    }

    /**
     * Loads all questions from the database into memory once per program run.
     */
    private static void ensureLoaded() {
        if (myLoaded) {
            return;
        }

        readQuestionsFromDatabase();
        Collections.shuffle(QUESTIONS);
        myNextIndex = 0;
        myLoaded = true;
    }

    /**
     * Reads rows from the SQLite questions table and converts them into
     * {@link AbstractTriviaQuestion} objects.
     */
    private static void readQuestionsFromDatabase() {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);

        final String sql = "SELECT QUESTION_TYPE, QUESTION, ANSWER, MC_INDEX FROM questions";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int id = 0;

            while (rs.next()) {
                final String type = rs.getString("QUESTION_TYPE");
                final String questionText = rs.getString("QUESTION");
                final String answer = rs.getString("ANSWER");

                // ID is just an in-memory sequence for now.
                id++;

                if ("MCQuestion".equals(type)) {
                    final Integer dbIndexObj = (Integer) rs.getObject("MC_INDEX");
                    if (dbIndexObj == null) {
                        continue;
                    }

                    int dbIndex = dbIndexObj.intValue();

                    // Support both 1-based and 0-based indexing.
                    // If the DB stores 1..N, convert to 0..N-1.
                    if (dbIndex > 0) {
                        dbIndex = dbIndex - 1;
                    }

                    final MultipleChoiceData mc = new MultipleChoiceData(answer, dbIndex);
                    final AbstractTriviaQuestion q = createQuestion(type, questionText, id, mc);
                    if (q != null) {
                        QUESTIONS.add(q);
                    }

                } else if ("SQuestion".equals(type)) {
                    final AbstractTriviaQuestion q = createQuestion(type, questionText, id, answer);
                    if (q != null) {
                        QUESTIONS.add(q);
                    }

                } else if ("TFQuestion".equals(type)) {
                    final String normalized = answer == null ? "false" : answer.trim().toLowerCase();
                    final AbstractTriviaQuestion q =
                            createQuestion(type, questionText, id, normalized);
                    if (q != null) {
                        QUESTIONS.add(q);
                    }
                }
            }

        } catch (final SQLException e) {
            throw new IllegalStateException("Failed to read questions from SQLite database", e);
        }
    }

    /**
     * Helper data container for multiple-choice questions.
     */
    public static final class MultipleChoiceData {

        private final List<String> myChoices;
        private final int myCorrectIndex;

        /**
         * Creates MC data from the DB format.
         *
         * @param theChoices comma+space separated list of choices (e.g., "A, B, C")
         * @param theCorrectIndex correct index (0-based)
         */
        public MultipleChoiceData(final String theChoices, final int theCorrectIndex) {
            final String[] array = theChoices == null ? new String[0] : theChoices.split(", ");
            myChoices = Arrays.asList(array);
            myCorrectIndex = theCorrectIndex;
        }

        public List<String> getChoices() {
            return myChoices;
        }

        public int getCorrectIndex() {
            return myCorrectIndex;
        }
    }
}