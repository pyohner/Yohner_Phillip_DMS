import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AttractionDatabase {

    private List<Attraction> attractions;

    public AttractionDatabase() {
        this.attractions = new ArrayList<>();
    }

    public void addAttractionFromFile(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String description = parts[2];
                String location = parts[3];
                String type = parts[4];
                String height = parts[5];
                int thrill = Integer.parseInt(parts[6]);
                LocalDate openingDate = LocalDate.parse(parts[7]);
                double rating = Double.parseDouble(parts[8]);
                attractions.add(new Attraction(id, name, description, location, type, height, thrill, openingDate, rating));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttractionManually() {
        Scanner attrInput = new Scanner(System.in);
        System.out.print("Enter an ID for the attraction: ");
        int id = attrInput.nextInt();
        attrInput.nextLine(); // Consume newline character

        System.out.print("Enter name of the attraction: ");
        String name = attrInput.nextLine();

        System.out.print("Enter a brief description of the attraction: ");
        String description = attrInput.nextLine();

        System.out.print("Enter location of the attraction: ");
        String location = attrInput.nextLine();

        System.out.print("Enter type of attraction: ");
        String type = attrInput.nextLine();

        System.out.print("Enter any height restriction for the attraction or 'None': ");
        String height = attrInput.nextLine();

        System.out.print("Enter the thrill level of the attraction (0-5): ");
        int thrill = attrInput.nextInt();
        attrInput.nextLine();

        System.out.print("Enter opening date of attraction (yyyy-mm-dd): ");
        LocalDate openingDate = LocalDate.parse(attrInput.nextLine());

        attractions.add(new Attraction(id, name, description, location, type, height, thrill, openingDate));
        System.out.println("Attraction added successfully.");
    }

    public void rateAttraction(int id, double rating) {
        Attraction attraction = getAttractionById(id);
        if (attraction != null) {
            attraction.addRating(rating);
            System.out.println("Rating added successfully.");
        } else {
            System.out.println("Attraction not found.");
        }
    }

    private Attraction getAttractionById(int id) {
        return attractions.stream().filter(attraction -> attraction.getId() == id).findFirst().orElse(null);
    }



    public void updateAttraction(int id) {
        Scanner scanner = new Scanner(System.in);
        Attraction attraction = getAttractionById(id);
        if (attraction == null) {
            System.out.println("Attraction not found.");
            return;
        }
        System.out.println("Selected: " + attraction.toString());

        System.out.println("Select the attribute to update:");
        System.out.println("1. Name");
        System.out.println("2. Description");
        System.out.println("3. Location");
        System.out.println("4. Type");
        System.out.println("5. Height Restriction");
        System.out.println("6. Thrill Level");
        System.out.println("7. Opening Date");
        int choice = scanner.nextInt();
        scanner.nextLine();  // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                attraction.setName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                attraction.setDescription(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new location: ");
                attraction.setLocation(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new type: ");
                attraction.setType(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new height restriction: ");
                attraction.setHeight(scanner.nextLine());
                break;
            case 6:
                System.out.print("Enter new thrill level: ");
                attraction.setThrill(scanner.nextInt());
                scanner.nextLine();
                break;
            case 7:
                System.out.print("Enter new opening date (yyyy-mm-dd): ");
                attraction.setOpeningDate(LocalDate.parse(scanner.nextLine()));
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
        System.out.println("Attraction updated successfully.");
    }

    public void removeAttraction(int attractionId) {
        Iterator<Attraction> iterator = attractions.iterator();
        while (iterator.hasNext()) {
            Attraction attraction = iterator.next();
            if (attraction.getId() == attractionId) {
                iterator.remove();
                System.out.println("Attraction with ID " + attractionId + " removed successfully.");
                return;
            }
        }
        System.out.println("Attraction with ID " + attractionId + " not found.");
    }

    public void getTopRatedAttractions() {
        List<Attraction> sortedAttractions = new ArrayList<>(attractions);
        sortedAttractions.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating()));
        List<Attraction> topAttractions = sortedAttractions.subList(0, Math.min(10, sortedAttractions.size()));
        System.out.println("Top Rated Attractions:");
        for (int i = 0; i < topAttractions.size(); i++) {
            Attraction attraction = topAttractions.get(i);
            System.out.printf("%d. %s (Description: %s, Location: %s, Type: %s, Height Restriction: %s, Thrill Level %d, Opening Date: %s, Average Rating: %.2f)%n",
                    i + 1, attraction.getName(), attraction.getDescription(), attraction.getLocation(), attraction.getType(),
                    attraction.getHeight(), attraction.getThrill(), attraction.getOpeningDate(), attraction.getAverageRating() /*attraction.getRating()*/);
        }
    }

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

}
