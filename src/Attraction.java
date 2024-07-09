/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * June 6, 2024
 *
 * Class: Attraction
 * This class defines the Attraction object and contains the get and set methods.
 *
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

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

    // Constructor - includes rating (used for initial data load-in)
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
    // Constructor - excludes rating
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

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeight() { return height; }

    public void setHeight(String height) { this.height = height; }

    public int getThrill() { return thrill; }

    public void setThrill(int thrill) { this.thrill = thrill; }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public void addRating (double rating) { ratings.add(rating); }

    public List<Double> getRatings() { return ratings; }

    /*
    Get Average Rating method
    Calculates the average rating from an attraction's ratings list and returns the average rating (double).
     */
    public double getAverageRating() {
        OptionalDouble average = ratings.stream().mapToDouble(Double::doubleValue).average();
        return average.isPresent() ? average.getAsDouble() : 0.0; // Returns the average of the object's ratings
    }

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