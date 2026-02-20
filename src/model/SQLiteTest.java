package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteDataSource;

/**
 *
 * @author peytonlaudanski
 * @version 1
 */
public class SQLiteTest {

    public static void main(final String[] theArgs) {
        SQLiteDataSource ds = null;

        // establish connection (creates db file if it does not exist :-)
        try {
            ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:questions.db");
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Opened database successfully");

        // now create a table
        String query = "CREATE TABLE IF NOT EXISTS questions ( " + "QUESTION TEXT NOT NULL, "
                + "ANSWER TEXT NOT NULL )";

        try (Connection conn = ds.getConnection() ;
                Statement stmt = conn.createStatement() ;) {

            final int rv = stmt.executeUpdate(query);
            System.out.println("executeUpdate() returned " + rv);
        } catch (final SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Created questions table successfully");

        // next insert two rows of data
        System.out.println("Attempting to insert two rows into questions table");

        final String query1 = "INSERT INTO questions ( QUESTION, ANSWER ) "
                + "VALUES ( 'Last name of Java creator?', 'Gosling' )";
        final String query2 = "INSERT INTO questions ( QUESTION, ANSWER ) "
                + "VALUES ( 'This statement is false', 'paradox' )";

        try (Connection conn = ds.getConnection() ;
                Statement stmt = conn.createStatement() ;) {
            int rv = stmt.executeUpdate(query1);
            System.out.println("1st executeUpdate() returned " + rv);

            rv = stmt.executeUpdate(query2);
            System.out.println("2nd executeUpdate() returned " + rv);

        } catch (final SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // now query the database table for all its contents and display the results
        System.out.println("Selecting all rows from test table");
        query = "SELECT * FROM questions";

        try (Connection conn = ds.getConnection() ;
                Statement stmt = conn.createStatement() ;) {

            final ResultSet rs = stmt.executeQuery(query);

            // walk through each 'row' of results, grab data by column/field name
            // and print it
            while (rs.next()) {
                final String question = rs.getString("QUESTION");
                final String answer = rs.getString("ANSWER");

                System.out.println("Result: Question = " + question + ", Answer = " + answer);
            }

            // query = "DELETE FROM questions";
            // final PreparedStatement pstmt = conn.prepareStatement(query);
            // pstmt.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

}
