import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Attraction {

    private int id;
    private String name;
    private String description;
    private String location;
    private String type;
    private String height;
    private int thrill;
    private LocalDate openingDate;
    //private double rating;
    private List<Double> ratings;

    public Attraction(int id, String name, String description, String location, String type, String height, int thrill, LocalDate openingDate, double ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.height = height;
        this.thrill = thrill;
        this.openingDate = openingDate;
        //this.rating = rating;
        this.ratings = new ArrayList<>();
        this.ratings.add(ratings);
    }
    public Attraction(int id, String name, String description, String location, String type, String height, int thrill, LocalDate openingDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.height = height;
        this.thrill = thrill;
        this.openingDate = openingDate;
        //this.rating = rating;
        this.ratings = new ArrayList<>();
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public double getAverageRating() {
        OptionalDouble average = ratings.stream().mapToDouble(Double::doubleValue).average();
        return average.isPresent() ? average.getAsDouble() : 0.0;
//        double average = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
//        return Math.round(average + 10.0)/10.0;
    }

    //public double getRating() {
    //    return rating;
    //}

    //public void setRating(double rating) {
    //    this.rating = rating;
    //}

    @Override
    public String toString() {
        return "Attraction{" +
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
