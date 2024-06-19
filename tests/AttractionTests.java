import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTests {

    Attraction attraction;  //  Create a test attraction object

    @BeforeEach
    void setUp() {
        //supply test data for the test attraction object
        attraction = new Attraction(100,
                "Mr. Toad's",
                "it's no more",
                "WDW-Fantsyland",
                "Dark ride",
                "none",
                1,
                LocalDate.parse("1972-10-01"),
                1.0 );
        attraction.addRating(2.0);
    }

    @Test
    @DisplayName("getID Test")
    void getId() {
        // Assert that the id is correctly retrieved
        assertEquals(attraction.getId(), 100, "Error retrieving ID (getID)");
    }

    @Test
    @DisplayName("setID Test")
    void setId() {
        attraction.setId(101); // Set ID
        // Assert that the id updated
        assertEquals(attraction.getId(), 101, "Error setting ID (setId)");
    }

    @Test
    @DisplayName("getName Test")
    void getName() {
        // Assert that the name is correctly retrieved
        assertEquals(attraction.getName(),"Mr. Toad's", "Error retrieving name (getName)" );
    }

    @Test
    @DisplayName("setName Test")
    void setName() {
        attraction.setName("Mr.Toad's Wild Ride");
        // Assert that the name updated correctly
        assertEquals(attraction.getName(),"Mr.Toad's Wild Ride", "Error setting name (setName)" );
    }

    @Test
    @DisplayName("getDescription Test")
    void getDescription() {
        // Assert that the description is correctly retrieved
        assertEquals(attraction.getDescription(), "it's no more", "Error getting description (getDescription)");
    }

    @Test
    @DisplayName("setDescription Test")
    void setDescription() {
        attraction.setDescription("A romping ride with Mr. Toad.");
        // Assert that the description updated correctly
        assertEquals(attraction.getDescription(), "A romping ride with Mr. Toad.", "Error setting description (setDescription)");
    }

    @Test
    @DisplayName("getLocation Test")
    void getLocation() {
        // Assert that the location is correctly retrieved
        assertEquals(attraction.getLocation(), "WDW-Fantsyland", "Error getting location (getLocation)");
    }

    @Test
    @DisplayName("setLocation Test")
    void setLocation() {
        attraction.setLocation("WDW-Fantasyland");
        // Assert that the location updated correctly
        assertEquals(attraction.getLocation(), "WDW-Fantasyland", "Error setting location (setLocation)");
    }

    @Test
    @DisplayName("getType Test")
    void getType() {
        // Assert that the type is correctly retrieved
        assertEquals(attraction.getType(), "Dark ride", "Error getting type (getType)");
    }

    @Test
    @DisplayName("setType Test")
    void setType() {
        attraction.setType("Slow car ride");
        assertEquals(attraction.getType(), "Slow car ride", "Error setting type (setType)");
    }

    @Test
    @DisplayName("getHeight Test")
    void getHeight() {
        // Assert that the height is correctly retrieved
        assertEquals(attraction.getHeight(), "none", "Error getting height (getHeight)");
    }

    @Test
    @DisplayName("setHeight Test")
    void setHeight() {
        attraction.setHeight("2 feet");
        assertEquals(attraction.getHeight(), "2 feet", "Error setting height (setHeight)");
    }

    @Test
    @DisplayName("getThrill Test")
    void getThrill() {
        // Assert that the thrill is correctly retrieved
        assertEquals(attraction.getThrill(), 1, "Error retrieving thrill (getThrill");
    }

    @Test
    @DisplayName("setThrill Test")
    void setThrill() {
        attraction.setThrill(3);
        assertEquals(attraction.getThrill(), 3, "Error setting thrill (setThrill)");
    }

    @Test
    @DisplayName("getOpeningDate Test")
    void getOpeningDate() {
        String dateEntered = "1972-10-01";
        // Assert that the opening date is correctly retrieved
        assertEquals(attraction.getOpeningDate(), LocalDate.parse(dateEntered), "Error retrieving opening date (getOpeningDate)");
    }

    @Test
    @DisplayName("setOpeningDate Test")
    void setOpeningDate() {
        attraction.setOpeningDate(LocalDate.of(1971, 10, 1));
        assertEquals(attraction.getOpeningDate(), LocalDate.of(1971,10,1), "Error setting opening date (setOpeningDate)");
    }

    @Test
    @DisplayName("addRating Test")
    void addRating() {
        int initialListSize = attraction.getRatings().size();
        attraction.addRating(3.0);
        assertEquals(attraction.getRatings().size(), initialListSize + 1, "Error adding rating (addRating)");
        assertTrue(attraction.getRatings().contains(3.0));
    }

    @Test
    @DisplayName("getRatings Test")
    void getRatings() {
        // Assert that the ratings are correctly retrieved
        assertEquals(attraction.getRatings().size(), 2, "Error retrieving ratings (getRatings)");
    }

    @Test
    @DisplayName("Compute Average Rating Test")
    void getAverageRating() {
        // using our own testing data, compute the average rating
        double sum = 0.0;
        int listSize = attraction.getRatings().size();
        for (Double value : attraction.getRatings()){
            sum += value;
        }
        Double averageRatingTest = sum / listSize;

        //assertEquals will compare two values and display a message is they aren't equal
        assertEquals(averageRatingTest, attraction.getAverageRating(),"Error: The average rating for the attraction is incorrect");

    }

    @Test
    void testToString() {
        String expectedString = "Attraction {" +
                "id=" + attraction.getId() +
                ", name='" + attraction.getName() + '\'' +
                ", description='" + attraction.getDescription() + '\'' +
                ", location='" + attraction.getLocation() + '\'' +
                ", type='" + attraction.getType() + '\'' +
                ", height='" + attraction.getHeight() + '\'' +
                ", thrill='" + attraction.getThrill() + '\'' +
                ", openingDate=" + attraction.getOpeningDate() +
                ", averageRating=" + attraction.getAverageRating() +
                '}';
        // Assert the toString displays as expected
        assertEquals(attraction.toString(), expectedString, "Error retrieving attraction toString");
    }
}