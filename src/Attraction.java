import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Attraction ---
 * This class defines the Attraction object and contains the get and set methods.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created June 6, 2024
 */
public class Attraction {

    // Attraction attributes
    private int id; // ID
    private String name; // Name
    private String description; // Description
    private String location; // Location
    private String type; // Type
    private String height; // Height Restrictions
    private int thrill; // Thrill Level
    private LocalDate openingDate; // Opening Date
    private List<Double> ratings; // Ratings

    /**
     * Attraction constructor (used for the initial data load-in)
     * @param id The attraction id
     * @param name The attraction name
     * @param description The attraction description
     * @param location The attraction location
     * @param type The attraction type
     * @param height The attraction height restriction
     * @param thrill The attraction thrill level
     * @param openingDate The attraction opening date
     * @param ratings An initial attraction rating
     */
    public Attraction(int id, String name, String description, String location, String type, String height, int thrill, LocalDate openingDate, double ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.height = height;
        this.thrill = thrill;
        this.openingDate = openingDate;
        this.ratings = new ArrayList<>();
        this.ratings.add(ratings);
    }

    /**
     * Attraction constructor (excludes rating)
     * @param id The attraction ID
     * @param name The attraction name
     * @param description The attraction description
     * @param location The attraction location
     * @param type The attraction type
     * @param height The attraction height restriction
     * @param thrill The attraction thrill level
     * @param openingDate The attraction opening date
     */
    public Attraction(int id, String name, String description, String location, String type, String height, int thrill, LocalDate openingDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.height = height;
        this.thrill = thrill;
        this.openingDate = openingDate;
        this.ratings = new ArrayList<>();
    }

    // Getters and Setters

    /**
     * @return Returns attraction ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The attraction ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * @return Returns the attraction name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The attraction name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the attraction description
     */
    public String getDescription() { return description; }

    /**
     * @param description The attraction description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * @return Returns the attraction location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The attraction location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Returns the attraction type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The attraction type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the attraction height restriction
     */
    public String getHeight() { return height; }

    /**
     * @param height The attraction height restriction
     */
    public void setHeight(String height) { this.height = height; }

    /**
     * @return Returns the attraction thrill level
     */
    public int getThrill() { return thrill; }

    /**
     * @param thrill The attraction thrill level
     */
    public void setThrill(int thrill) { this.thrill = thrill; }

    /**
     * @return Returns the attraction opening date
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * @param openingDate The attraction opening date
     */
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * @param rating The attraction rating to be added
     */
    public void addRating (double rating) { ratings.add(rating); }

    /**
     * @return Returns the ratings list
     */
    public List<Double> getRatings() { return ratings; }

    /**
     * Calculates the average rating from an attraction's ratings list and returns the average rating (double).
     * @return Returns the average rating
     */
    public double getAverageRating() {
        OptionalDouble average = ratings.stream().mapToDouble(Double::doubleValue).average();
        return average.isPresent() ? average.getAsDouble() : 0.0; // Returns the average of the object's ratings
    }

    /**
     * @return Returns the attraction details in a String
     */
    @Override
    public String toString() {
        return "Attraction {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", height='" + height + '\'' +
                ", thrill='" + thrill + '\'' +
                ", openingDate=" + openingDate +
                ", averageRating=" + getAverageRating() +
                '}';
    }
}