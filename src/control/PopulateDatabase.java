package control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sqlite.SQLiteDataSource;

/**
 * USE THIS CLASS TO POPULATE THE DATABASE WITH QUESTIONS AND ANSWERS. Run this ONCE before
 * running the game. If you need to reset: delete questions.db and run again. NOTE: Some
 * question content was generated with AI assistance to test the GUI.
 * 
 * @author peytonlaudanski, huyle
 * @version 2
 */
public final class PopulateDatabase {

    /**
     *
     */
    private PopulateDatabase() {
    }

    /**
     *
     * @param theArgs
     */
    public static void main(final String[] theArgs) {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:questions.db");

        System.out.println("Opened database successfully");
        // Fixed: Added ID, TYPE, and CHOICES columns so QuestionFactory can build
        // the correct question subclass (SHORT, TF, MC) when reading from the database.
        final String createTable = "CREATE TABLE IF NOT EXISTS questions ( "
                + "ID       INTEGER PRIMARY KEY AUTOINCREMENT, " + "TYPE     TEXT NOT NULL, "
                + "QUESTION TEXT NOT NULL, " + "ANSWER   TEXT NOT NULL, " + "CHOICES  TEXT )";

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTable);
            System.out.println("Created questions table successfully");
        } catch (final SQLException e) {
            Logger.getLogger(PopulateDatabase.class.getName()).log(Level.SEVERE, "", e);
            System.exit(0);
        }

        // Clear old data so re-running does not duplicate rows.
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            // Fixed: Clears old rows before inserting so re-running this class
            // does not duplicate questions in the database.
            stmt.executeUpdate("DELETE FROM questions");
        } catch (final SQLException e) {
            Logger.getLogger(PopulateDatabase.class.getName()).log(Level.SEVERE, "", e);

            System.exit(0);
        }

        final String[][] shortQ = {{"Last name of the Java creator?", "Gosling"},
                                   {"What planet is closest to the Sun?", "Mercury"},
                                   {"How many sides does a hexagon have?", "6"},
                                   {"What is the chemical symbol for water?", "H2O"},
                                   {"What country has the largest population?", "China"},
                                   {"How many players on a basketball team?", "5"},
                                   {"What is 12 multiplied by 12?", "144"},
                                   {"What is the capital of Japan?", "Tokyo"},};

        final String[][] tfQ =
                {{"The Great Wall of China is visible from space.", "false"},
                 {"The Earth orbits the Sun.", "true"}, {"Spiders are insects.", "false"},
                 {"Water boils at 100 degrees Celsius.", "true"},
                 {"The Amazon is the longest river in the world.", "false"},
                 {"Humans have 206 bones.", "true"},
                 {"Sound travels faster than light.", "false"},
                 {"A group of crows is called a murder.", "true"},};

        // Format: { question, correct answer, "choice1|choice2|choice3|choice4" }
        // Correct answer must match one of the pipe-separated choices exactly.
        final String[][] mcQ =
                {{"What is 2 + 2?", "4", "3|4|5|6"},
                 {"Which planet is known as the Red Planet?", "Mars",
                  "Venus|Mars|Jupiter|Saturn"},
                 {"What is the capital of France?", "Paris", "London|Berlin|Paris|Rome"},
                 {"Which language is Java most similar to?", "C++", "Python|C++|Ruby|Swift"},
                 {"How many bytes are in a kilobyte?", "1024", "512|1024|2048|4096"},
                 {"Who painted the Mona Lisa?", "Leonardo da Vinci",
                  "Michelangelo|Raphael|Leonardo da Vinci|Donatello"},
                 {"What is the largest ocean on Earth?", "Pacific",
                  "Atlantic|Indian|Pacific|Arctic"},
                 {"Which data structure uses LIFO order?", "Stack", "Queue|Stack|List|Tree"},};

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {

            for (final String[] q : shortQ) {
                stmt.executeUpdate(
                        "INSERT INTO questions (TYPE,QUESTION,ANSWER,CHOICES) VALUES ("
                                + "'SHORT','" + q[0] + "','" + q[1] + "',NULL)");
            }
            System.out.println("Inserted " + shortQ.length + " short answer questions");

            for (final String[] q : tfQ) {
                stmt.executeUpdate(
                        "INSERT INTO questions (TYPE,QUESTION,ANSWER,CHOICES) VALUES ("
                                + "'TF','" + q[0] + "','" + q[1] + "',NULL)");
            }
            System.out.println("Inserted " + tfQ.length + " true/false questions");

            for (final String[] q : mcQ) {
                stmt.executeUpdate(
                        "INSERT INTO questions (TYPE,QUESTION,ANSWER,CHOICES) VALUES ("
                                + "'MC','" + q[0] + "','" + q[1] + "','" + q[2] + "')");
            }
            System.out.println("Inserted " + mcQ.length + " multiple choice questions");

        } catch (final SQLException e) {
            Logger.getLogger(PopulateDatabase.class.getName()).log(Level.SEVERE, "", e);
            System.exit(0);
        }

        // Verify contents.
        System.out.println("\n=== Verifying database contents ===");
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM questions")) {
            int count = 0;
            while (resultSet.next()) {
                count++;
                System.out.println("[" + resultSet.getString("TYPE") + "] "
                        + resultSet.getString("QUESTION") + " -> "
                        + resultSet.getString("ANSWER"));
            }
            System.out.println("\nTotal: " + count + " questions.");
        } catch (final SQLException e) {
            Logger.getLogger(PopulateDatabase.class.getName()).log(Level.SEVERE, "", e);
            System.exit(0);
        }

        System.out.println("\nDone! Now run MazeController.main() to play.");
    }
}