/*
* Phillip Yohner
* CEN 3024C - 31950
* June 6, 2024
*
* Class: MainApp
* This is the main class for the Disney Attractions DMS application.
* In this version, the application will load sample attraction data
* from a text file, list the attractions, and then present a menu for
* the user.  The user can choose to add an attraction, remove an
* attraction, update an attraction, rate an attraction, list the
* Top 10 rated attractions, or list all attractions. They can also
* choose to exit the program.
* - Adding an attraction will ask the user for a name, description,
*    location, type, height restriction, thrill level, and opening date.
* - Removing an attraction will ask the user for the ID number of the
*    attraction they want to remove or rate.
* - Updating an attraction will ask the user for the ID number of the
*    attraction they want to update, then which attribute they want to
*    update before asking for the updated information.
* - Rating an attraction will ask the user for the ID number of the
*    attraction they want to remove or rate.
* - Listing the Top 10 attractions will calculate the average rating for
*    each attraction and then list the top 10 of a rating sorted list.
* - Listing all attractions will display all the attractions in the database.
*
* Update: June 27, 2024
* Implemented graphical user interface (GUI). User activities remain the
* same, except for added feature to allow user to load in their own file.
* User menus designed using Swing UI Designer.
* Console activities commented-out (disabled).
*
*/

import javax.swing.*;
import java.awt.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        // Splash screen for a little magic
        JWindow splashScreen = new JWindow();
        JPanel content = (JPanel) splashScreen.getContentPane();
        content.setBackground(Color.white);
        // Splash screen size and location
        int width = 400;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        splashScreen.setBounds(x, y, width, height);
        // Add an image and loading message
        JLabel label = new JLabel(new ImageIcon(MainApp.class.getResource("/resources/mickey-ears1-100px.png")));
        JLabel loadingText = new JLabel("Loading something magical, please wait...", JLabel.CENTER);
        content.add(label, BorderLayout.CENTER);
        content.add(loadingText, BorderLayout.SOUTH);
        // Display the splash screen
        splashScreen.setVisible(true);

        // Create database(list) object for the attractions
        AttractionDatabase attractionDatabase = new AttractionDatabase();
        // Create Scanner for user inputs
        Scanner input = new Scanner(System.in);
        // The filepath to the text file with comma separated values
        String filePath = "/resources/attractions.txt";
        System.out.println("Loading data...");
        // addAttractionFromFile adds the list from the text file to the database(list)
        attractionDatabase.addAttractionsFromFile(filePath);
        // listAttractions displays all attractions
        attractionDatabase.listAttractions();

        System.out.println("\n*************************************************");
        System.out.println("*                                               *");
        System.out.println("*           Disney Attractions DMS              *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");

        try {
            Thread.sleep(3000); // Provide a little loading time (3 seconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Remove splash screen
        splashScreen.setVisible(false);
        splashScreen.dispose();

        // Create main menu
        MainMenuFrame mainFrame = new MainMenuFrame(attractionDatabase);

        // ******  See MainMenuFrame and UpdateMenu classes for GUI actions.  ******

        /*
        // ******  Console-based activities are below...  ******

        int choice; // integer variable for menu selection from user
        boolean exit = false; // menu exit variable

        // do loop to repeat main menu until user chooses to exit
        do {
            try {

                // Display user menu
                System.out.println("\nMenu:");
                System.out.println("1. Add an attraction manually");
                System.out.println("2. Update an attraction");
                System.out.println("3. Remove an attraction");
                System.out.println("4. Rate an attraction");
                System.out.println("5. List top 10 attractions");
                System.out.println("6. List all attractions");
                System.out.println("7. Exit");
                System.out.print("\nEnter your choice (1-7): ");

                choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1: // Add an attraction
                        attractionDatabase.addAttractionManually();
                        break;
                    case 2: // Update an attraction
                        // Prompt the user to enter the ID of the attraction to update
                        System.out.print("Enter the ID of the attraction to update: ");
                        int attractionToUpdate = input.nextInt(); // ID input
                        // Call updateAttraction with entered ID number (attractionToUpdate)
                        attractionDatabase.updateAttraction(attractionToUpdate);
                        input.nextLine(); // clear input
                        break;
                    case 3: // Remove an attraction
                        // Ask user if they want to enter an ID or enter the name and location?
                        System.out.println("Remove by Attraction ID or Attraction Name and Location?: ");
                        System.out.println("1. Enter Attraction ID");
                        System.out.println("2. Enter Attraction Name and Location");
                        int removeOption = input.nextInt();
                        input.nextLine(); //clear input
                        switch (removeOption) {
                            case 1:
                                // Prompt the user to enter the ID of the attraction to remove
                                System.out.print("Enter the ID of the attraction to remove: ");
                                int attrIdToRemove = input.nextInt(); // ID input
                                //input.nextLine(); //clear input
                                // Call removeAttraction with entered ID number (removeAttraction (int))
                                attractionDatabase.removeAttraction(attrIdToRemove);
                                break;
                            case 2:
                                // Prompt the user to enter the name of the attraction to remove
                                System.out.println("Enter the name of the attraction: ");
                                String attrNameToRemove = input.nextLine();
                                // Prompt the user to enter the location of the attraction to remove
                                System.out.println("Enter the location of the attraction: ");
                                String attrLocationToRemove = input.nextLine();
                                // Call removeAttraction with entered name and location (removeAttraction (String, String))
                                attractionDatabase.removeAttraction(attrNameToRemove, attrLocationToRemove);
                                break;
                        }
                        break;
                    case 4: // Rate an attraction
                        // Ask user how
                        // Prompt the user to enter the ID of the attraction to rate
                        System.out.print("Enter the ID of the attraction to rate: ");
                        int attractionToRate = input.nextInt(); // ID input
                        // Prompt the user to enter a rating for the attraction
                        System.out.print("Enter rating (0.0 to 5.0): ");
                        double rating = input.nextDouble(); // Rating input
                        // Call rateAttraction with entered ID (attractionToRate) and rating
                        attractionDatabase.rateAttraction(attractionToRate, rating);
                        input.nextLine(); // clear input
                        break;
                    case 5: // List the top 10 rated attractions
                        // Call getTopRatedAttractions to list the 10 highest average rating
                        attractionDatabase.getTopRatedAttractions();
                        break;
                    case 6: // List all attractions in the database
                        // Call listAttractions to list all attractions
                        attractionDatabase.listAttractions();
                        break;
                    case 7: // Exit the program
                        exit = true;
                        System.out.println("Exiting... See ya real soon!"); // Exit message
                        break;
                    default: // Invalid choice message
                        System.out.println("Invalid choice. Please enter a valid option."); // Invalid choice message
                }
            } catch (InputMismatchException e) { // Catches InputMismatchExceptions
                System.out.println("Invalid entry. Please try again."); // Invalid choice message
                input.nextLine(); // clear input
            }
        } while (!exit); // Exit procedure
        input.close(); // close input Scanner

         */
    }
}