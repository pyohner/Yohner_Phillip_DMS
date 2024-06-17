import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


class AttractionTestOLD {

    //create an object to be tested

    Attraction attraction;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //supply test data to an attraction object
        attraction = new Attraction(100,
                "Mr. Toad's",
                "it's no more",
                "WDW-Fantasyland",
                "Dark ride",
                "none",
                1,
                LocalDate.of(1972, 10, 1),
                1.0 );
        attraction.addRating(2.0);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Computer Average Rating Test")
    void getAverageRatingTest() {
        //using our own testing data, compute the average rating
        double sum = 0.0;
        int listSize = attraction.getRatings().size();
        for (Double value : attraction.getRatings()){
            sum += value;
        }
        Double averageRatingTest = sum / listSize;

        //assertEquals will compare two values and display a message is they aren't equal
        assertEquals(averageRatingTest, attraction.getAverageRating(),"Error: The average rating for the attraction is incorrect");
    }



}