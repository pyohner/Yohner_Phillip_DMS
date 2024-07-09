/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * June 6, 2024
 *
 * Class: AttractionDatabase
 * This is the database class for the Disney Attractions DMS application.
 * All database CRUD (create, remove, update, delete) methods and calculations
 * are found in this class.
 *
 * Update: June 27, 2024
 * Class updated for GUI functionality. Console actions commented-out.
 *
 * Update: July 9, 2024
 * Class updated for database connectivity.
 * Array/Text file database management commented-out.
 */

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.DoubleStream;
//import java.io.*;
//import java.nio.charset.StandardCharsets;

public class AttractionDatabase extends Component {
    private Connection conn;
    private static List<Attraction> attractions; // list containing Attraction objects
    private int maxThrill = 5;
//    public static int listSize;

    public AttractionDatabase() {
        this.conn = DatabaseConnection.connect();
        System.out.println("Creating Attractions table...");
        createAttrTable();
        System.out.println("Creating Ratings table...");
        createRatingsTable();
    }

    /*
    Create Attractions Table method
    Creates an attractions table in the database if one doesn't exist.
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

    /*
    Create Ratings Table method
    Creates a ratings table in the database if one doesn't exist.
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

    /*
    Add Attraction Manually method
    Asks the user to enter the details for each attribute of the attraction.
    The ID is auto-generated.
    Adds the attraction to the database.
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

    /*
    Rate Attraction method for GUI & database
    Prompts the user for the rating.
    Gets the attraction by ID and adds the rating to the attraction's rating list.
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

    /*
    Remove Attraction method (1 of 2)
    Takes the attraction ID and iterates through the attractions list for matches.
    The attraction with the matching ID is removed from the list.
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

    /*
    Remove Attraction method (2 of 2)
    Takes the attraction name and location searches through the attractions list for matches.
    The attraction with the matching name and location is removed from the list.
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

    /*
    Get All Attractions method
    Gathers all attractions data from the database.
    Returns a list of the attractions' data.
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

    /*
    Get Attraction by ID method
    Takes in an integer as the ID number and returns the attraction object with the matching ID,
    otherwise, will return 'null' if no match is found.
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

    /*
    Is Unique Attraction method
    Takes in a name and location (Strings) and checks if that combination exists in the attraction list.
    Returns 'true' when unique or 'false' when not unique.
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

    /*
    Get Attractions method
    Returns the current list of the attractions' data.
     */
    public List<Attraction> getAttractions() {
        return attractions;
    }

    /*
    Get Top Ten method
    Gathers the top 10 rated attractions.
    Returns the list of the attractions' data.
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

    /*
    Get Average Rating method
    Gathers ratings with the same attraction id and calculates the average.
    Returns the average rating value for the attraction.
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

    /*
    Get Database Size method
    Returns the number of attractions in the database.
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

    /*
    Update Name method
    Takes the attraction id and new name and updates the name.
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

    /*
    Update Description method
    Takes the attraction id and new description and updates the description.
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

    /*
    Update Location method
    Takes the attraction id and new location and updates the location.
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

    /*
    Update Type method
    Takes the attraction id and new type and updates the type.
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

    /*
    Update Height method
    Takes the attraction id and new height restriction and updates the height.
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

    /*
    Update Thrill method
    Takes the attraction id and new thrill level and updates the thrill.
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

    /*
    Update Opening Date method
    Takes the attraction id and new opening date and updates the opening date.
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


    /*
    // ******  Array/Text File activities are below...  ******

//    public void addAttraction(Attraction attraction) {
//
//        String sql = "INSERT INTO attractions(id, name, description, location, type, height, thrill, openingDate, rating) VALUES(?,?,?,?,?,?,?,?,?)";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(2, attraction.getName());
//            pstmt.setString(3, attraction.getDescription());
//            pstmt.setString(4, attraction.getLocation());
//            pstmt.setString(5, attraction.getType());
//            pstmt.setString(6, attraction.getHeight());
//            pstmt.setInt(7, attraction.getThrill());
//            pstmt.setDate(8, Date.valueOf(attraction.getOpeningDate()));
//            pstmt.setDouble(9, attraction.getAverageRating());
//            pstmt.execute();
//            System.out.println("Attraction added: " + attraction);
//        } catch (SQLException s){
//            System.out.println(s.getMessage());
//        }
//
//    }

    /*
    addAttractionsFromFile reads the text file, parses the information one line at a time, and adds
    the attraction to the list
    This works for the automated load-in only.  See manualAttractionsFromFile for non-automated process.
    */
//    public void addAttractionsFromFile(String filePath) {
//        try (InputStream inputStream = getClass().getResourceAsStream(filePath);
//             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
//        {
//            String sql = "INSERT INTO attractions(id, name, description, location, type, height, thrill, openingDate, rating) VALUES(?,?,?,?,?,?,?,?,?)";
//            String ratingSql = "INSERT INTO ratings(attrId, rating) VALUES(?,?)";
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                int id = Integer.parseInt(parts[0]); // ID
//                String name = parts[1]; // Name
//                String description = parts[2]; // Description
//                String location = parts[3]; // Location
//                String type = parts[4]; // Type
//                String height = parts[5]; // Height Restrictions
//                int thrill = Integer.parseInt(parts[6]); // Thrill Level
//                LocalDate openingDate = LocalDate.parse(parts[7]); // Opening Date
//                double rating = Double.parseDouble(parts[8]); // initial Rating
//                // Create Attraction object with parsed data and add to list
//                Attraction attrToAdd = new Attraction(id, name, description, location, type, height, thrill, openingDate, rating);
//                Double ratingToAdd = rating;
//                if (!!isUniqueAttraction(attrToAdd.getName(), attrToAdd.getLocation())) {
//                    try (Connection conn = DatabaseConnection.connect();
//                         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS) ) {
//                        pstmt.setString(2, attrToAdd.getName());
//                        pstmt.setString(3, attrToAdd.getDescription());
//                        pstmt.setString(4, attrToAdd.getLocation());
//                        pstmt.setString(5, attrToAdd.getType());
//                        pstmt.setString(6, attrToAdd.getHeight());
//                        pstmt.setInt(7, attrToAdd.getThrill());
//                        pstmt.setDate(8, Date.valueOf(attrToAdd.getOpeningDate()));
//                        pstmt.setDouble(9, attrToAdd.getAverageRating());
//                        pstmt.executeUpdate();
//                        int returnedId;
//                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()){
//                            if (generatedKeys.next()){
//                                returnedId = generatedKeys.getInt(1);
//                            } else {
//                                throw new SQLException("Creating attraction failed, no ID obtained.");
//                            }
//                        }
//                        PreparedStatement ratingpstmt = conn.prepareStatement(ratingSql);
//                        ratingpstmt.setInt(1, returnedId);
//                        ratingpstmt.setDouble(2, rating);
//                        ratingpstmt.executeUpdate();
//                    } catch (SQLException s) {
//                        System.out.println(s.getMessage());
//                    }
//                    System.out.println(attrToAdd.getName() + " at " + attrToAdd.getLocation() + " added.");
//                } else {
//                System.out.println(attrToAdd.getName() + " at " + attrToAdd.getLocation() + " already exists. Not added.");
//                }
//            }
//        } catch (IOException e) { // Catches IOException. Will print stack trace and application will exit
//            JOptionPane.showMessageDialog(new JOptionPane(), "Failed to load data from file. Check file location.", "Failed to Load", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Failed to load data from file. Check file location.");
//            e.getMessage();
//        } catch (NullPointerException n) {
//            JOptionPane.showMessageDialog(new JOptionPane(), "Failed to load data from file. Check file location.", "Failed to Load", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Failed to load data from file. Check file location.");
//            n.getMessage();
//        }
//    }
    /*
    manualAttractionsFromFile reads the text file at the provided location, parses the information one line at a time,
    and adds the attractions to the list
    This works for the manual load-in only.  See addAttractionsFromFile for automated process.
    */
//    public void manualAttractionsFromFile(String filePath) {
//        try (
//        BufferedReader br = new BufferedReader(new FileReader(filePath)))
//        {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                int id = Integer.parseInt(parts[0]); // ID
//                String name = parts[1]; // Name
//                String description = parts[2]; // Description
//                String location = parts[3]; // Location
//                String type = parts[4]; // Type
//                String height = parts[5]; // Height Restrictions
//                int thrill = Integer.parseInt(parts[6]); // Thrill Level
//                LocalDate openingDate = LocalDate.parse(parts[7]); // Opening Date
//                double rating = Double.parseDouble(parts[8]); // initial Rating
//                // Create Attraction object with parsed data and add to list
//                attractions.add(new Attraction(id, name, description, location, type, height, thrill, openingDate, rating));
////                listSize++;
//            }
//        } catch (IOException e) { // Catches IOException. Will print stack trace and application will exit
//            JOptionPane.showMessageDialog(new JOptionPane(), "Failed to load data from file. Check file location.", "Failed to Load", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Failed to load data from file. Check file location.");
//        } catch (NullPointerException n) {
//            JOptionPane.showMessageDialog(new JOptionPane(), "Failed to load data from file. Check file location.", "Failed to Load", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Failed to load data from file. Check file location.");
//        }
//    }

     /*
    Rate Attraction method
    Accepts the attraction ID and rating.
    Gets the attraction by ID and adds the rating to the attraction's rating list.
     */
//    public void rateAttraction(int id, double rating) {
//        Attraction attraction = getAttractionById(id); // Get attraction by ID
//        if (attraction != null) { // If ID exists...
//            if (rating >= 0.0 && rating <= 5.0) {
//                attraction.addRating(rating); // Add rating to attraction
//                System.out.println("Rating added successfully.");
//            } else {
//                System.out.println("Invalid rating.");
//            }
//        } else { // If ID doesn't exist, let user know
//            System.out.println("Attraction not found.");
//        }
//    }
    /*
    Update Attraction method
    Accepts the attraction ID.
    Gets attraction by ID.  Asks the user for the attribute to update, then asks for the new value.
    Updates the attraction in the database.
     */
//    public void updateAttraction(int id) {
//        Scanner userInput = new Scanner(System.in); // create input Scanner
//        Attraction attraction = getAttractionById(id); // Get attraction by ID
//        if (attraction == null) { // If ID doesn't exist, let user know
//            JOptionPane.showMessageDialog(this, "Attraction not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Attraction not found.");
//            return;
//        }
//
//        boolean exit = false; // menu exit flag
//
//            String name = JOptionPane.showInputDialog("Enter the attraction name: ");
//
//        // Update Attraction menu
//        do {
//            try {
//                System.out.println("Selected: " + attraction.toString()); // Shows selected attraction
//                System.out.println("Select the attribute to update:");
//                System.out.println("1. Name");
//                System.out.println("2. Description");
//                System.out.println("3. Location");
//                System.out.println("4. Type");
//                System.out.println("5. Height Restriction");
//                System.out.println("6. Thrill Level");
//                System.out.println("7. Opening Date");
//                System.out.println("8. Return to Main Menu");
//                int choice = userInput.nextInt(); // User selection
//                userInput.nextLine();  // clear input
//
//                switch (choice) {
//                    case 1: // New Name entry
//                        System.out.print("Enter new name: ");
//                        String nameInput = userInput.nextLine();
//                        // Checks if name and location combination already exists.
//                        if (isUniqueAttraction(nameInput, attraction.getLocation())) { // If unique...
//                            attraction.setName(nameInput); // Updates name.
//                            System.out.println("Attraction name updated successfully.");
//                        } else { // If not unique, notify user and do not update name.
//                            System.out.println("An attraction with this name already exists at this location.");
//                            System.out.println("Name not updated.");
//                        }
//                        break;
//                    case 2: // New Description entry
//                        System.out.print("Enter new description: ");
//                        attraction.setDescription(userInput.nextLine());
//                        System.out.println("Attraction description updated successfully.");
//                        break;
//                    case 3: // New Location entry
//                        System.out.print("Enter new location: ");
//                        String locationInput = userInput.nextLine();
//                        // Checks if name and location combination already exists.
//                        if (isUniqueAttraction(attraction.getName(), locationInput)) { // If unique...
//                            attraction.setLocation(userInput.nextLine()); // Updates location.
//                            System.out.println("Attraction location updated successfully.");
//                        } else { // If not unique, notify user and do not update location.
//                            System.out.println("This location you entered already has this attraction.");
//                            System.out.println("Location not updated.");
//                        }
//                        break;
//                    case 4: // New Type entry
//                        System.out.print("Enter new type: ");
//                        attraction.setType(userInput.nextLine());
//                        System.out.println("Attraction type updated successfully.");
//                        break;
//                    case 5: // New Height Restriction entry
//                        System.out.print("Enter new height restriction: ");
//                        attraction.setHeight(userInput.nextLine());
//                        System.out.println("Attraction height restriction updated successfully.");
//                        break;
//                    case 6: // New Thrill Level entry
//                        boolean isThrillValid = false;
//                        do {
//                            try {
//                                System.out.print("Enter new thrill level: ");
//                                int thrill = userInput.nextInt();
//                                while (thrill < 0 || thrill > maxThrill) {
//                                    System.out.print("Invalid thrill level. Please try again. \nEnter the thrill level of the attraction (0-5): ");
//                                    thrill = userInput.nextInt(); // retake thrill level input
//                                }
//                                attraction.setThrill(thrill);
//                                userInput.nextLine();
//                                isThrillValid = true;
//                                System.out.println("Attraction thrill level updated successfully.");
//                            } catch (InputMismatchException m) {
//                                System.out.println("Invalid thrill level. Please try again.");
//                                userInput.nextLine();
//                            }
//                        } while (!isThrillValid);
//                        break;
//                    case 7: // New Opening Date entry
//                        boolean isDateValid = false;
//                        do {
//                            try {
//                                System.out.print("Enter new opening date (yyyy-mm-dd): ");
//                                attraction.setOpeningDate(LocalDate.parse(userInput.nextLine()));
//                                isDateValid = true;
//                                System.out.println("Attraction opening date updated successfully.");
//                            } catch (DateTimeException d) {
//                                System.out.println("Invalid date. Please try again");
//                            }
//                        } while (!isDateValid);
//                        break;
//                    case 8: // Exit option - No change made
//                        break;
//                    default: // Invalid choice message
//                        System.out.println("Invalid choice.");
//                        break;
//                }
//                exit = true; // Ok to exit
//            } catch (InputMismatchException e) { // catches InputMismatchException and restarts menu
//                System.out.println("Invalid entry. Please try again.");
//                userInput.nextLine(); // clear input
//            } catch (DateTimeException d) { // catches DateTimeException and restarts menu
//                System.out.println("Invalid entry. Please try again.");
//            }
//        } while (!exit); // Exit to main menu
//    }
/*
    Get Top Rated Attractions method
    Lists the top 10 rated attractions.
     */
//    public void getTopRatedAttractions() {
//        List<Attraction> sortedAttractions = new ArrayList<>(attractions); // create a copy of the attractions list
//        sortedAttractions.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating())); // sort the list by average rating
//        List<Attraction> topAttractions = sortedAttractions.subList(0, Math.min(10, sortedAttractions.size())); // copy the top 10 to a topAttraction list
//        System.out.println("Top Rated Attractions:");
//        // Display the data for each of the Top Attractions
//        for (int i = 0; i < topAttractions.size(); i++) {
//            Attraction attraction = topAttractions.get(i);
//            System.out.printf("#%d %s (ID: %d, Description: %s, Location: %s, Type: %s, Height Restriction: %s, Thrill Level %d, Opening Date: %s, Average Rating: %.2f)%n",
//                    i + 1, attraction.getName(), attraction.getId(), attraction.getDescription(), attraction.getLocation(), attraction.getType(),
//                    attraction.getHeight(), attraction.getThrill(), attraction.getOpeningDate(), attraction.getAverageRating() /*attraction.getRating()*/);
//        }
//    }

//    public String viewTopRatedAttractions() {
//        List<Attraction> sortedAttractions = new ArrayList<>(attractions); // create a copy of the attractions list
//        sortedAttractions.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating())); // sort the list by average rating
//        List<Attraction> topAttractions = sortedAttractions.subList(0, Math.min(10, sortedAttractions.size())); // copy the top 10 to a topAttraction list
//        StringBuilder sb = new StringBuilder();
//        for (Attraction attraction : topAttractions) {
//            sb.append(attraction.toString()).append("\n");
//        }
//        return sb.toString();
//    }

    /*
    List Attractions method
    Displays the full list of attractions.
     */
//    public void listAttractions() {
//        System.out.println("Listing all attractions:");
//        for (Attraction attraction : attractions) {
//            System.out.println("ID: " + attraction.getId() +
//                    ", Name: " + attraction.getName() +
//                    ", Description: " + attraction.getDescription() +
//                    ", Location: " + attraction.getLocation() +
//                    ", Type: " + attraction.getType() +
//                    ", Height Restriction: " + attraction.getHeight() +
//                    ", Thrill Level: " + attraction.getThrill() +
//                    ", Opening Date: " + attraction.getOpeningDate() +
//                    ", Average Rating: " + attraction.getAverageRating());
//        }
//    }

//    public String viewAttractions() {
//        StringBuilder sb = new StringBuilder();
//        for (Attraction attraction : attractions) {
//            sb.append(attraction.toString()).append("\n");
//        }
//        return sb.toString();
//    }

}