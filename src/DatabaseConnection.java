import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection ---
 * This class establishes connection to the database.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created July 9, 2024
 */

public class DatabaseConnection {

    private static String dbFilePath;
    private static String classPath = "jdbc:sqlite:";

    /**
     * Asks the user for the file path to the database file.
     * @return Returns a connection to the database
     */
    public static Connection connect() {
        if (dbFilePath == null) {
            dbFilePath = JOptionPane.showInputDialog("Enter the path of the database file: ");
        }

        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(classPath + dbFilePath);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException c){
            System.out.println("SQLite JDBC driver not found.");
            c.getMessage();
        }
        return conn;
    }
}