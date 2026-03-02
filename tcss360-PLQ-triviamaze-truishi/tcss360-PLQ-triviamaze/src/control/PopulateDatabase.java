package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.SQLiteDataSource;

/**
 * PopulateDatabase
 *
 * One-time utility for creating and populating the SQLite database.
 *
 * IMPORTANT: The game should READ from the database at runtime, but SHOULD NOT
 * populate it at runtime. This class exists so the team can add questions
 * (or regenerate the DB) outside gameplay.
 *
 * Table schema used by the game:
 *   questions(QUESTION_TYPE TEXT NOT NULL,
 *             QUESTION      TEXT NOT NULL,
 *             ANSWER        TEXT NOT NULL,
 *             MC_INDEX      INTEGER)
 *
 * Conventions:
 * - SQuestion: ANSWER = short answer, MC_INDEX = NULL
 * - TFQuestion: ANSWER = "true" or "false" (any case), MC_INDEX = NULL
 * - MCQuestion: ANSWER = "choice1, choice2, choice3", MC_INDEX = correct index
 *   (1-based or 0-based; QuestionFactory supports both).
 *
 * @author peytonlaudanski
 * @author truishi
 * @version 2
 */
public final class PopulateDatabase {

    /** SQLite URL (relative to working directory). */
    private static final String DB_URL = "jdbc:sqlite:questions.db";

    private PopulateDatabase() {
        // utility
    }

    /**
     * Creates the table (if needed) and inserts a few sample questions.
     */
    public static void main(final String[] theArgs) {

        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);

        try (Connection conn = dataSource.getConnection()) {
            createTableIfMissing(conn);

            // ===== Add questions here (examples) =====
            insertShort(conn, "Last name of Java creator?", "Gosling");
            insertTrueFalse(conn, "1 + 1 = 2", true);
            insertMultipleChoice(conn, "What is 2 + 2?", "1, 2, 3, 4", 4);

            // ===== Print out what is in the table =====
            printAll(conn);

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfMissing(final Connection theConn) throws SQLException {
        final String sql =
                "CREATE TABLE IF NOT EXISTS questions (" +
                "QUESTION_TYPE TEXT NOT NULL," +
                "QUESTION TEXT NOT NULL," +
                "ANSWER TEXT NOT NULL," +
                "MC_INDEX INTEGER" +
                ")";

        try (PreparedStatement ps = theConn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    /** Inserts a short-answer question. */
    public static void insertShort(final Connection theConn, final String theQuestion,
            final String theAnswer) throws SQLException {

        final String sql =
                "INSERT INTO questions (QUESTION_TYPE, QUESTION, ANSWER, MC_INDEX) " +
                "VALUES (?, ?, ?, NULL)";

        try (PreparedStatement ps = theConn.prepareStatement(sql)) {
            ps.setString(1, "SQuestion");
            ps.setString(2, theQuestion);
            ps.setString(3, theAnswer);
            ps.executeUpdate();
        }
    }

    /** Inserts a true/false question. */
    public static void insertTrueFalse(final Connection theConn, final String theQuestion,
            final boolean theAnswer) throws SQLException {

        final String sql =
                "INSERT INTO questions (QUESTION_TYPE, QUESTION, ANSWER, MC_INDEX) " +
                "VALUES (?, ?, ?, NULL)";

        try (PreparedStatement ps = theConn.prepareStatement(sql)) {
            ps.setString(1, "TFQuestion");
            ps.setString(2, theQuestion);
            ps.setString(3, String.valueOf(theAnswer));
            ps.executeUpdate();
        }
    }

    /** Inserts a multiple-choice question. */
    public static void insertMultipleChoice(final Connection theConn, final String theQuestion,
            final String theChoicesCsv, final int theCorrectIndex) throws SQLException {

        final String sql =
                "INSERT INTO questions (QUESTION_TYPE, QUESTION, ANSWER, MC_INDEX) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = theConn.prepareStatement(sql)) {
            ps.setString(1, "MCQuestion");
            ps.setString(2, theQuestion);
            ps.setString(3, theChoicesCsv);
            ps.setInt(4, theCorrectIndex);
            ps.executeUpdate();
        }
    }

    private static void printAll(final Connection theConn) throws SQLException {
        try (PreparedStatement ps = theConn.prepareStatement(
                "SELECT QUESTION_TYPE, QUESTION, ANSWER, MC_INDEX FROM questions");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                final String type = rs.getString("QUESTION_TYPE");
                final String q = rs.getString("QUESTION");
                final String a = rs.getString("ANSWER");
                final Object idx = rs.getObject("MC_INDEX");

                System.out.println(type + " | Q=" + q + " | A=" + a + " | MC_INDEX=" + idx);
            }
        }
    }
}