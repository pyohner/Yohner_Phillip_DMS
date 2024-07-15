import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * AttractionDatabase ---
 * This is the database class for the Disney Attractions DMS application.
 * All database CRUD (create, remove, update, delete) methods and calculations
 * are found in this class.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created June 6, 2024
 */
public class AttractionDatabase extends Component {
    private Connection conn; // Database connection
    private static List<Attraction> attractions; // List containing Attraction objects
    private int maxThrill = 5; // Max thrill level

    /**
     * Creates the necessary tables in the database.
     */
    public AttractionDatabase() {
        this.conn = DatabaseConnection.connect();
        System.out.println("Creating Attractions table...");
        createAttrTable();
        System.out.println("Creating Ratings table...");
        createRatingsTable();
    }

    /**
     * Creates an attractions table in the database if one doesn't exist.
     */
    private void createAttrTable() {
        String sql = "CREATE TABLE IF NOT EXISTS attractions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "height TEXT NOT NULL, " +
                "thrill INTEGER NOT NULL, " +
                "openingDate DATE NOT NULL, " +
                "rating REAL NULL)";

        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("Attractions table ready.");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    /**
     * Creates a ratings table in the database if one doesn't exist.
     */
    private void createRatingsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                "attrId INTEGER NOT NULL, " +
                "rating REAL NOT NULL)";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("Ratings table ready.");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    /**
     * Adds an attraction to the database.
     * Asks the user to enter the details for each attribute of the attraction.
     * The ID is auto-generated.
     */
    public void addAttractionManually() {
        boolean exit = false; //  menu exit flag

        // Add Attraction menu
        do {
            try {
                String name = JOptionPane.showInputDialog("Enter the attraction name: ");
                String description = JOptionPane.showInputDialog("Enter a description: ");
                String location = JOptionPane.showInputDialog("Enter the location: ");
                String type = JOptionPane.showInputDialog("Enter type: ");
                String height = JOptionPane.showInputDialog("Enter height restriction: ");
                boolean isThrillValid = false;
                int thrill = 0; // Initialize thrill level
                do {
                    try {
                        thrill = Integer.parseInt(JOptionPane.showInputDialog("Enter thrill level (0-5): "));
                        // Prompts user to only enter values 0 through 5. Will repeat until successful.
                        while (thrill < 0 || thrill > maxThrill) {
                            JOptionPane.showMessageDialog(this, "Invalid thrill level. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                            thrill = Integer.parseInt(JOptionPane.showInputDialog("Enter thrill level: "));
                        }
                        isThrillValid = true;
                    } catch (InputMismatchException m) { // Catches InputMismatchExceptions. Will repeat until successful.
                        JOptionPane.showMessageDialog(this, "Invalid thrill level. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Invalid thrill level. Please try again.");
                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(this, "Invalid thrill level. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Invalid thrill level. Please try again.");
                    }
                } while (!isThrillValid);

                boolean isDateValid = false;
                LocalDate openingDate = LocalDate.now(); // Initialize Opening Date
                do {
                    try {
                        openingDate = LocalDate.parse(JOptionPane.showInputDialog("Enter opening date (yyyy-mm-dd): "));
                        isDateValid = true;
                    } catch (DateTimeException d) { // Catches DateTimeExceptions. Will repeat until successful.
                        JOptionPane.showMessageDialog(this, "Invalid date. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Invalid date. Please try again");
                    }
                } while (!isDateValid);

                // Check that attraction name and location combination is unique
                if (isUniqueAttraction(name, location)) { // If name & location is unique...
                    // Create Attraction with data and add to database
                    String sql = "INSERT INTO attractions(name, description, location, type, height, thrill, openingDate) VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                        pstmt.setString(1, name);
                        pstmt.setString(2, description);
                        pstmt.setString(3, location);
                        pstmt.setString(4, type);
                        pstmt.setString(5, height);
                        pstmt.setInt(6, thrill);
                        pstmt.setDate(7, Date.valueOf(openingDate));
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Attraction added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Attraction added successfully.");
                    } catch (SQLException s) {
                        System.out.println(s.getMessage());
                    }
                } else { // If name & location is not unique, notify user
                    JOptionPane.showMessageDialog(this, "This attraction name and location already exists.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("This attraction name and location already exists.");
                }
                exit = true; // Ok to exit to main menu
            } catch (InputMismatchException e) { // catches InputMismatchException and will restart menu
                JOptionPane.showMessageDialog(this, "Invalid entry. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Invalid entry. Please try again.");
            } catch (DateTimeException d) { // catches DateTimeException and will restart menu
                JOptionPane.showMessageDialog(this, "Invalid entry. Please try again.", "Try again", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Invalid entry. Please try again.");
            }
        } while (!exit); // Exit to main menu
    }

    /**
    * Prompts the user for the attraction ID and rating.
    * Gets the attraction by ID and adds the rating to the attraction's rating list.
    */
    public void rateAttraction() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter attraction ID: "));
            Attraction attraction = getAttractionById(id); // Get attraction by ID
            if (attraction != null) { // If ID exists...
                double rating = Double.parseDouble(JOptionPane.showInputDialog("Enter rating: "));
                if (rating >= 0.0 && rating <= 5.0) {

                    String sql = "INSERT INTO ratings(attrId, rating) VALUES(?, ?)";
                    try (Connection conn = DatabaseConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, id);
                        pstmt.setDouble(2, rating);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Attraction rated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                    Double averageRating = getAverageRating(id);
                    String sql2 = "UPDATE attractions " +
                            "SET rating = " + averageRating +
                            " WHERE id = " + id;
                    try (Connection conn = DatabaseConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid rating.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { // If ID doesn't exist, let user know
                JOptionPane.showMessageDialog(this, "Attraction not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException n){
            JOptionPane.showMessageDialog(this, "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Takes the attraction ID and iterates through the attractions list for matches.
     * The attraction with the matching ID is removed from the list.
     * @param attractionId The attraction ID
     */
    public void removeAttraction(int attractionId) {

        Attraction attrToRemove = getAttractionById(attractionId);

        String sql = "DELETE FROM attractions WHERE id = " + attractionId;
        String ratingSql = "DELETE FROM ratings WHERE attrId = " + attractionId;

        if (attrToRemove != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(new JOptionPane(), attrToRemove + " removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException s) {
                System.out.println(s.getMessage());
            }
            try (PreparedStatement ratingpstmt = conn.prepareStatement(ratingSql)) {
                ratingpstmt.executeUpdate();
            } catch (SQLException s) {
                System.out.println(s.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(new JOptionPane(), "Attraction with ID " + attractionId + " not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Takes the attraction name and location searches through the attractions list for matches.
     * The attraction with the matching name and location is removed from the list.
     * @param name The attraction name
     * @param location The attraction location
     * @throws SQLException Throws a SQL Exception
     */
    public void removeAttraction(String name, String location) throws SQLException {
        String sql = "SELECT id FROM attractions WHERE name = ? AND location = ?";
        ResultSet rs = null;
        int idToRemove = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            rs = pstmt.executeQuery();
            idToRemove = rs.getInt("id");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

        if(idToRemove > 0) {
            String removeRatingsSql = "DELETE FROM ratings WHERE attrId = " + idToRemove;
            String removeAttrSql = "DELETE FROM attractions WHERE id = " + idToRemove;

            try (PreparedStatement pstmt = conn.prepareStatement(removeRatingsSql)) {
                pstmt.executeUpdate();
            } catch (SQLException s) {
                System.out.println(s.getMessage());
            }
            try (PreparedStatement pstmt = conn.prepareStatement(removeAttrSql)) {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(new JOptionPane(), name + " (id: " + idToRemove + ") removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException s) {
                System.out.println(s.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(new JOptionPane(), "Attraction named " + name + " at " + location + " not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Gathers all attractions data from the database.
     * Returns a list of the attractions' data.
     * @return Returns list of all attractions.
     */
    public List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        String sql = "SELECT * FROM attractions";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Attraction attraction = new Attraction(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("height"),
                        rs.getInt("thrill"),
                        rs.getDate("openingDate").toLocalDate(),
                        rs.getDouble("rating")
                );
                attractions.add(attraction);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return attractions;
    }

    /**
     * Takes in an integer as the ID number and returns the attraction object with the matching ID,
     * otherwise, will return 'null' if no match is found.
     * @param id The attraction ID
     * @return Returns the attraction.
     */
    static Attraction getAttractionById(int id) {

        String sql = "SELECT * FROM attractions WHERE id = " + id;
        Attraction attraction = null;
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                attraction = new Attraction(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("height"),
                        rs.getInt("thrill"),
                        rs.getDate("openingDate").toLocalDate(),
                        rs.getDouble("rating") );
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        return attraction;
    }

    /**
     * Takes in a name and location (Strings) and checks if that combination exists in the attraction list.
     * Returns 'true' when unique or 'false' when not unique.
     * @param name The attraction name
     * @param location The attraction location
     * @return true or false
     */
    public boolean isUniqueAttraction(String name, String location) {
        List<Attraction> attractions = new ArrayList<>();
        String sql = "SELECT * FROM attractions";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Attraction attraction = new Attraction(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("height"),
                        rs.getInt("thrill"),
                        rs.getDate("openingDate").toLocalDate(),
                        rs.getDouble("rating")
                );
                attractions.add(attraction);
            }
        } catch (SQLException s){
            System.out.println(s.getMessage());
        }
        for (Attraction attraction : attractions) {
            if (attraction.getName().equalsIgnoreCase(name) && attraction.getLocation().equalsIgnoreCase(location)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the current list of the attractions' data.
     * @return Returns all attractions
     */
    public List<Attraction> getAttractions() {
        return attractions;
    }

    /**
     * Gathers the top 10 rated attractions.
     * Returns the list of the attractions' data.
     * @return Returns top 10 rated attractions
     */
    public List<Attraction> getTopTen() {
        List<Attraction> topAttractions = new ArrayList<>();
        String sql = "SELECT * FROM attractions ORDER BY rating DESC LIMIT 10;";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Attraction attraction = new Attraction(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("height"),
                        rs.getInt("thrill"),
                        rs.getDate("openingDate").toLocalDate(),
                        rs.getDouble("rating")
                );
                topAttractions.add(attraction);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topAttractions;
    }

    /**
     * Gathers ratings with the same attraction id and calculates the average.
     * Returns the average rating value for the attraction.
     * @param id The attraction's ID
     * @return Returns average rating
     */
    public double getAverageRating(int id) {
        String sql = "SELECT rating from ratings WHERE attrId = " + id;
        List<Double> ratings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Double foundRating = rs.getDouble("rating");
                ratings.add(foundRating);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DoubleStream doubleStream = ratings.stream().mapToDouble(Double::doubleValue);
        OptionalDouble optionalAverage = doubleStream.average();
        System.out.println("Average Rating calculated: " + optionalAverage);
        return optionalAverage.isPresent() ? optionalAverage.getAsDouble() : 0.0;
    }

    /**
     * Returns the number of attractions in the database.
     * @return Returns number of attractions
     */
    public int getDatabaseSize(){
        int tableSize = 0;
        String sql = "SELECT COUNT(*) FROM attractions";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                tableSize = rs.getInt(1);
            } else {
                throw new SQLException("Failed to get the table size");
            }
        } catch (SQLException s){
            System.out.println(s.getMessage());
        }
        return tableSize;
    }

    /**
     * Takes the attraction id and new name and updates the name.
     * @param id The attraction ID
     * @param name The attraction.
     */
    public void updateName(int id, String name) {
        String sql = "UPDATE attractions SET name = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Name updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new description and updates the description.
     * @param id The attraction ID
     * @param description The attraction description
     */
    public void updateDescription(int id, String description) {
        String sql = "UPDATE attractions SET description = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Description updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new location and updates the location.
     * @param id The attraction ID
     * @param location The attraction location
     */
    public void updateLocation(int id, String location) {
        String sql = "UPDATE attractions SET location = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, location);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Location updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new type and updates the type.
     * @param id The attraction ID
     * @param type The attraction type
     */
    public void updateType(int id, String type) {
        String sql = "UPDATE attractions SET type = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Type updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new height restriction and updates the height.
     * @param id The attraction ID
     * @param height The attraction height
     */
    public void updateHeight(int id, String height) {
        String sql = "UPDATE attractions SET height = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, height);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Height restriction updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new thrill level and updates the thrill.
     * @param id The attraction ID
     * @param thrill The attraction thrill level (0-5)
     */
    public void updateThrill(int id, int thrill) {
        String sql = "UPDATE attractions SET thrill = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, thrill);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Thrill level updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }

    /**
     * Takes the attraction id and new opening date and updates the opening date.
     * @param id The attraction ID
     * @param openingDate The attraction opening date (yyyy-mm-dd)
     */
    public void updateOpeningDate(int id, LocalDate openingDate) {
        String sql = "UPDATE attractions SET openingDate = ? WHERE id = " + id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(openingDate));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(new JOptionPane(), "Opening date updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException s) {
            s.getMessage();
        }
    }
}