/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * June 6, 2024
 *
 * Class: Attraction Database
 * This is the database class for the Disney Attractions DMS application.
 * All database CRUD (create, remove, update, delete) methods and calculations
 * are found in this class.
 *
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

public class AttractionDatabase {

    private List<Attraction> attractions; // list containing Attraction objects

    public AttractionDatabase() {
        this.attractions = new ArrayList<>(); // create the list to store attractions
    }

    // addAttractionsFromFile reads the text file, parses the information one line at a time, and adds
    // the attraction to the list
    public void addAttractionsFromFile(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
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
            }
        } catch (IOException e) { // Catches IOException. Will print stack trace and application will exit
            System.out.println("Failed to load data from file. Check file location in MainApp");
            e.printStackTrace();
        }
    }

    // Add Attraction method
    public void addAttractionManually() {
        Scanner attrInput = new Scanner(System.in); // create Scanner for user input
        boolean exit = false; //  menu exit flag
        int nextId = 0; // integer for highest ID number
        // for loop looks for the max ID value in the list and adds 1 to generate a new ID number
        for(Attraction attraction : attractions){
            if (attraction.getId() >= nextId){
                nextId = attraction.getId() + 1;
            }
        }

        // Add Attraction menu
        do {
            try {
                System.out.print("Enter name of the attraction: ");
                String name = attrInput.nextLine(); // attraction name input

                System.out.print("Enter a brief description of the attraction: ");
                String description = attrInput.nextLine(); // description input

                System.out.print("Enter location of the attraction: ");
                String location = attrInput.nextLine(); //  location input

                System.out.print("Enter type of attraction: ");
                String type = attrInput.nextLine(); // type input

                System.out.print("Enter any height restriction for the attraction or 'None': ");
                String height = attrInput.nextLine(); // height restrictions input

                System.out.print("Enter the thrill level of the attraction (0-5): ");
                int thrill = attrInput.nextInt(); // thrill level input
                // Prompts user to only enter values 0 through 5. Will repeat until successful.
                while (thrill < 0 || thrill > 5){
                    System.out.print("Invalid. \nEnter the thrill level of the attraction (0-5): ");
                    thrill = attrInput.nextInt(); // retake thrill level input
                }
                attrInput.nextLine(); // clear input

                System.out.print("Enter opening date of attraction (yyyy-mm-dd): ");
                LocalDate openingDate = LocalDate.parse(attrInput.nextLine()); // opening date input
                // Create Attraction object with data and add to list
                attractions.add(new Attraction(nextId, name, description, location, type, height, thrill, openingDate));
                System.out.println("Attraction added successfully.");
                exit = true; // Ok to exit to main menu
            } catch (InputMismatchException e) { // catches InputMismatchException and will restart menu
                System.out.println("Invalid entry. Please try again.");
                attrInput.nextLine(); // clear input
            } catch (DateTimeException d) { // catches DateTimeException and will restart menu
                System.out.println("Invalid entry. Please try again.");
            }
        } while (!exit); // Exit to main menu
    }

    // Rate Attraction method
    // Gets attraction by ID and adds the rating to the attraction's object
    public void rateAttraction(int id, double rating) {
        Attraction attraction = getAttractionById(id); // Get attraction by ID
        if (attraction != null) { // If ID exists...
            attraction.addRating(rating); // Add rating to attraction
            System.out.println("Rating added successfully.");
        } else { // If ID doesn't exist, let user know
            System.out.println("Attraction not found.");
        }
    }

    // Update Attraction method
    // Gets attraction by ID, asks the user for the attribute to update, then
    // asks for the new value and updates the attraction.
    public void updateAttraction(int id) {
        Scanner userInput = new Scanner(System.in); // create input Scanner
        Attraction attraction = getAttractionById(id); // Get attraction by ID
        if (attraction == null) { // If ID doesn't exist, let user know
            System.out.println("Attraction not found.");
            return;
        }
        boolean exit = false; // menu exit flag

        // Update Attraction menu
        do {
            try{
            System.out.println("Selected: " + attraction.toString()); // Shows selected attraction
            System.out.println("Select the attribute to update:");
            System.out.println("1. Name");
            System.out.println("2. Description");
            System.out.println("3. Location");
            System.out.println("4. Type");
            System.out.println("5. Height Restriction");
            System.out.println("6. Thrill Level");
            System.out.println("7. Opening Date");
            System.out.println("8. Return to Main Menu");
            int choice = userInput.nextInt(); // User selection
            userInput.nextLine();  // clear input

            switch (choice) {
                case 1: // New Name entry
                    System.out.print("Enter new name: ");
                    attraction.setName(userInput.nextLine());
                    System.out.println("Attraction name updated successfully.");
                    break;
                case 2: // New Description entry
                    System.out.print("Enter new description: ");
                    attraction.setDescription(userInput.nextLine());
                    System.out.println("Attraction description updated successfully.");
                    break;
                case 3: // New Location entry
                    System.out.print("Enter new location: ");
                    attraction.setLocation(userInput.nextLine());
                    System.out.println("Attraction location updated successfully.");
                    break;
                case 4: // New Type entry
                    System.out.print("Enter new type: ");
                    attraction.setType(userInput.nextLine());
                    System.out.println("Attraction type updated successfully.");
                    break;
                case 5: // New Height Restriction entry
                    System.out.print("Enter new height restriction: ");
                    attraction.setHeight(userInput.nextLine());
                    System.out.println("Attraction height restriction updated successfully.");
                    break;
                case 6: // New Thrill Level entry
                    System.out.print("Enter new thrill level: ");
                    attraction.setThrill(userInput.nextInt());
                    userInput.nextLine();
                    System.out.println("Attraction thrill level updated successfully.");
                    break;
                case 7: // New Opening Date entry
                    System.out.print("Enter new opening date (yyyy-mm-dd): ");
                    attraction.setOpeningDate(LocalDate.parse(userInput.nextLine()));
                    System.out.println("Attraction opening date updated successfully.");
                    break;
                case 8: // Exit option - No change made
                    break;
                default: // Invalid choice message
                    System.out.println("Invalid choice.");
                    break;
            }
            exit = true; // Ok to exit
            } catch (InputMismatchException e){ // catches InputMismatchException and restarts menu
                System.out.println("Invalid entry. Please try again.");
                userInput.nextLine(); // clear input
            } catch (DateTimeException d) { // catches DateTimeException and restarts menu
                System.out.println("Invalid entry. Please try again.");
            }
        } while (!exit); // Exit to main menu
    }

    // Remove Attraction method
    // Takes the attraction ID and iterates through the attractions list for matches.
    // The attraction with the matching ID is removed from the list.
    public void removeAttraction(int attractionId) {
        Iterator<Attraction> iterator = attractions.iterator(); // Create iterator for attractions list
        while (iterator.hasNext()) { // Go through each attraction
            Attraction attraction = iterator.next(); // Take the next attraction
            if (attraction.getId() == attractionId) { // Does the attraction ID match the user's entry?
                iterator.remove(); // If so, remove this attraction from the list.
                System.out.println("Attraction with ID " + attractionId + " removed successfully.");
                return;
            }
        }
        System.out.println("Attraction with ID " + attractionId + " not found."); // Let user know if no match exists
    }

    // Get Top Rated Attractions method
    // Lists the top 10 rated attractions
    public void getTopRatedAttractions() {
        List<Attraction> sortedAttractions = new ArrayList<>(attractions); // create a copy of the attractions list
        sortedAttractions.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating())); // sort the list by average rating
        List<Attraction> topAttractions = sortedAttractions.subList(0, Math.min(10, sortedAttractions.size())); // copy the top 10 to a topAttraction list
        System.out.println("Top Rated Attractions:");
        // Display the data for each of the Top Attractions
        for (int i = 0; i < topAttractions.size(); i++) {
            Attraction attraction = topAttractions.get(i);
            System.out.printf("#%d %s (ID: %d, Description: %s, Location: %s, Type: %s, Height Restriction: %s, Thrill Level %d, Opening Date: %s, Average Rating: %.2f)%n",
                    i + 1, attraction.getName(), attraction.getId(), attraction.getDescription(), attraction.getLocation(), attraction.getType(),
                    attraction.getHeight(), attraction.getThrill(), attraction.getOpeningDate(), attraction.getAverageRating() /*attraction.getRating()*/);
        }
    }

    // List Attractions method
    // Simply displays the full list of attractions.
    public void listAttractions() {
        System.out.println("Listing all attractions:");
        for (Attraction attraction : attractions) {
            System.out.println("ID: " + attraction.getId() +
                    ", Name: " + attraction.getName() +
                    ", Description: " + attraction.getDescription() +
                    ", Location: " + attraction.getLocation() +
                    ", Type: " + attraction.getType() +
                    ", Height Restriction: " + attraction.getHeight() +
                    ", Thrill Level: " + attraction.getThrill() +
                    ", Opening Date: " + attraction.getOpeningDate() +
                    ", Average Rating: " + attraction.getAverageRating());
        }
    }

    // Get Attraction by ID method
    // Takes in an integer as the ID number and returns the attraction object with the matching ID,
    // otherwise, will return 'null' if no match is found.
    private Attraction getAttractionById(int id) {
        return attractions.stream().filter(attraction -> attraction.getId() == id).findFirst().orElse(null);
    }
}