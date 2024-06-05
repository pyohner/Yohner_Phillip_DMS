import java.util.Scanner;

public class MainApp {
    public static void main(String[] args){

        // Create database(list) object
        AttractionDatabase attractionDatabase = new AttractionDatabase();
        // Create Scanner for user inputs
        Scanner input = new Scanner(System.in);
        String filePath = "C:/Users/yohnep25/IdeaProjects/DisneyAttrDMS/src/attractions.txt";
        attractionDatabase.addAttractionFromFile(filePath);
        attractionDatabase.listAttractions();

        int choice;
        do {
            // Display menu options to user
            System.out.println("\nMenu:");
            System.out.println("1. Add an attraction manually");
            System.out.println("2. Remove an attraction");
            System.out.println("3. Update an attraction");
            System.out.println("4. Rate an attraction");
            System.out.println("5. List top 10 attractions");
            System.out.println("6. List all attractions");
            System.out.println("7. Quit");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    // Add an attraction
                    attractionDatabase.addAttractionManually();
                    break;
                case 2:
                    // Prompt the user to enter the ID of the attraction to remove
                    System.out.print("Enter the ID of the attraction to remove: ");
                    int attractionToRemove = input.nextInt();
                    attractionDatabase.removeAttraction(attractionToRemove);
                    input.nextLine();
                    break;
                case 3:
                    // Prompt the user to enter the ID of the attraction to remove
                    System.out.print("Enter the ID of the attraction to update: ");
                    int attractionToUpdate = input.nextInt();
                    attractionDatabase.updateAttraction(attractionToUpdate);
                    input.nextLine();
                    break;
                case 4:
                    // Prompt the user to enter the ID of the attraction to rate
                    System.out.print("Enter the ID of the attraction to rate: ");
                    int attractionToRate = input.nextInt();
                    System.out.print("Enter rating (0.0 to 5.0): ");
                    double rating = input.nextDouble();
                    attractionDatabase.rateAttraction(attractionToRate, rating);
                    input.nextLine();
                    break;
                case 5:
                    // List the top 10 rated attractions
                    attractionDatabase.getTopRatedAttractions();
                    break;
                case 6:
                    // List all attractions in the database
                    attractionDatabase.listAttractions();
                    break;
                case 7:
                    // Quit the program
                    System.out.println("Exiting...");
                    break;
                default:
                    // Invalid choice message
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 7);
        input.close();

    }

}
