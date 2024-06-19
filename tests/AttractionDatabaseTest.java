/* Phillip Yohner
 * CEN 3024C - 31950
 * June 19, 2024
 *
 * Class: AttractionDatabaseTest
 * This is a test class to test all methods.
 *
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AttractionDatabaseTest {
    private List<Attraction> attractions; // list containing Attraction objects
    public AttractionDatabaseTest() {
        this.attractions = new ArrayList<>(); // create the list to store attractions
    }

    String filePath = "src/attractions.txt";

    @BeforeEach
    void setUp() {
        // Create two test attractions
        attractions.add(new Attraction(100,
                "Mr. Toad's",
                "it's no more",
                "WDW-Fantsyland",
                "Dark ride",
                "none",
                1,
                LocalDate.parse("1972-10-01"),
                1.0 ));

        attractions.add(new Attraction(101,
                "Snow White's Scary Adventure",
                "it's old",
                "WDW-Fantasyland",
                "Dark ride",
                "none",
                1,
                LocalDate.parse("1972-10-01"),
                1.0 ));
    }

    @Test
    @DisplayName("addAttractionsFromFile Test")
    // Add attractions from file test
    void addAttractionsFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        int startingSize = 2;
        int listSize = startingSize;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]); // ID
            String name = parts[1]; // Name
            String description = parts[2]; // Description
            String location = parts[3]; // Location
            String type = parts[4]; // Type
            String height = parts[5]; // Height Restrictions
            int thrill = Integer.parseInt(parts[6]); // Thrill Level
            LocalDate openingDate = LocalDate.parse(parts[7]); // Opening Date
            double rating = Double.parseDouble(parts[8]); // initial Rating
            // Create Attraction object with parsed data and add to list
            attractions.add(new Attraction(id, name, description, location, type, height, thrill, openingDate, rating));
            listSize++;
        }
        // Assert that the attractions were added from file - tests list size
        assertEquals(attractions.size(), listSize , "Error adding attractions from file.");
        assertTrue(listSize>startingSize, "Error adding attractions from file.");
    }

    @Test
    @DisplayName("addAttractionsManually Test")
    void addAttractionManually() throws DateTimeException {
        int nextId = 0;
        for (Attraction attraction : attractions) {
            if (attraction.getId() >= nextId) {
                nextId = attraction.getId() + 1;
            }
        }
        String name = "Tron";
        String description = "A new attraction.";
        String location = "WDW-Tomorrowland";
        String type = "Roller coaster";
        String height = "none";
        int thrill = 4;
        String dateEntered = "2023-10-10";
        LocalDate openingDate = LocalDate.parse(dateEntered);
        Attraction newAttraction = new Attraction(nextId, name, description, location, type, height, thrill, openingDate);
        int listSize = attractions.size();
        attractions.add(newAttraction);
        // Assert the attraction was added - tests list size and the list contains the attraction
        assertEquals(attractions.size(),listSize + 1, "Error adding attraction (addAttractionManually)" );
        assertTrue(attractions.contains(newAttraction), "Error finding added attraction (addAttractionManually)");
    }

    @Test
    @DisplayName("rateAttraction Test")
    void rateAttraction() throws NullPointerException {
        int id = 100;
        double rating = 3.0;
        Attraction matchingAttr = attractions.stream().filter(attraction -> attraction.getId() == id).findFirst().orElse(null); // Get attraction by ID
        int startSize = matchingAttr.getRatings().size();
        matchingAttr.addRating(rating);
        // Assert the rating was added - tests list size
        assertEquals(matchingAttr.getRatings().size(), startSize + 1 , "Error adding rating (rateAttraction)");
    }

    @Test
    @DisplayName("updateAttraction Test")
    void updateAttraction() throws DateTimeException, NullPointerException {
        int id = 100;
        Attraction matchingAttr = attractions.stream().filter(attraction1 -> attraction1.getId() == id).findFirst().orElse(null); // Get attraction by ID
        matchingAttr.setName("Switch up");
        matchingAttr.setDescription("Something updated.");
        matchingAttr.setLocation("WDW-Main Street");
        matchingAttr.setType("Dark ride");
        matchingAttr.setHeight("3 feet 6 inches");
        matchingAttr.setThrill(2);
        String dateEntered = "2010-10-10";
        matchingAttr.setOpeningDate(LocalDate.parse(dateEntered));
        // Assert tha attraction was updated - compares attraction at id location
        assertEquals(matchingAttr, attractions.stream().filter(attraction1 -> attraction1.getId() == id).findFirst().orElse(null), "Error updating attraction.");
    }

    @Test
    @DisplayName("removeAttraction by ID Test")
    void removeAttractionById() {
        int id = 100;
        Attraction matchingAttr = attractions.stream().filter(attraction1 -> attraction1.getId() == id).findFirst().orElse(null); // Get attraction by ID
        int startSize = attractions.size();
        attractions.removeIf(attraction -> attraction.getId() == id);
        // Assert attraction was removed - tests list size and if list contains attraction
        assertEquals(attractions.size(),startSize - 1, "Error removing attraction by ID (removeAttractionById");
        assertFalse(attractions.contains(matchingAttr));
    }

    @Test
    @DisplayName("removeAttraction by Name and Location Test")
    void removeAttractionByNameLocation() {
        int startSize = attractions.size();
        String name = "Snow White's Scary Adventure";
        String location = "WDW-Fantasyland";
        List<Attraction> matchingAttractions = new ArrayList<>(); // Create a list to hold matching attractions
        for (Attraction attraction : attractions) { // Go through each attraction in the list
            if (attraction.getName().equalsIgnoreCase(name) && attraction.getLocation().equalsIgnoreCase(location)) { // When name and location matches...
                matchingAttractions.add(attraction); // Add matching attraction to matchingAttractions list
            }
        }
        assertEquals(matchingAttractions.size(), 1, "Error finding just one match (removeAttractionByNameLocation)");
        Attraction matchingAttr = matchingAttractions.get(0);
        attractions.remove(matchingAttr);
        // Assert attraction was removed - tests list size and if list contains attraction
        assertEquals(attractions.size(),startSize - 1, "Error removing attraction by ID (removeAttractionById");
        assertFalse(attractions.contains(matchingAttr));
    }

    @Test
    @DisplayName("getTopRatedAttractions Test")
    void getTopRatedAttractions() {
        int listSize = attractions.size();
        List<Attraction> sortedAttractions = new ArrayList<>(attractions); // create a copy of the attractions list
        sortedAttractions.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating())); // sort the list by average rating
        List<Attraction> topAttractions = sortedAttractions.subList(0, Math.min(10, sortedAttractions.size())); // copy the top 10 to a topAttraction list
        Attraction firstSortedAttraction = sortedAttractions.get(0);
        Attraction topAttraction = topAttractions.get(0);
        Attraction tenthSortedAttraction = sortedAttractions.get(listSize-1);
        Attraction lastTopAttraction = topAttractions.get(listSize-1);
        // Assert top attractions are correct - tests for first and last attraction and list size
        assertEquals(firstSortedAttraction, topAttraction, "Error finding 1st attraction (getTopRatedAttractions)");
        assertEquals(tenthSortedAttraction, lastTopAttraction, "Error finding 10th attraction (getTopRatedAttractions)");
        assertEquals(topAttractions.size(), listSize, "Error finding Top 10 (getTopRatedAttractions)");
    }
}