/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * July 9, 2024
 *
 * Class: DatabaseConnection
 * This class establishes connection to the database.
 *
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static String dbFilePath;
    private static String classPath = "jdbc:sqlite:";
//    private static final String URL = "jdbc:sqlite:C:/Users/yohnep25/java_databases/sqlite/db/disney_attractions.db";

    /*
    Connection method
    Asks the user for the file path to the database file.
    A connection is returned.
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