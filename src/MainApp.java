import javax.swing.*;
import java.awt.*;

/**
 * Disney Attractions DMS (MainApp) ---
 * The user can choose to add an attraction, remove an attraction,
 * update an attraction, rate an attraction, list the
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
 * git https://github.com/pyohner/Yohner_Phillip_DMS
 * @version 1.4.2
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created June 6, 2024
 */
public class MainApp {

    /**
     * Main class to run application.
     * @param args A string array containing the command line arguments
     */
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

        // Create database(list) object for the attractions
        AttractionDatabase attractionDatabase = new AttractionDatabase();
        // Display the splash screen
        splashScreen.setVisible(true);// Create Scanner for user inputs

        // Console title "Disney Attractions DMS"
        System.out.println("\n*****************************************************************************************************************");
        System.out.println("*    ____  _                          ___   __  __                  __  _                     ____  __  ________*");
        System.out.println("*   / __ \\(_)________  ___  __  __   /   | / /_/ /__________ ______/ /_(_)___  ____  _____   / __ \\/  |/  / ___/*");
        System.out.println("*  / / / / / ___/ __ \\/ _ \\/ / / /  / /| |/ __/ __/ ___/ __ `/ ___/ __/ / __ \\/ __ \\/ ___/  / / / / /|_/ /\\__ \\ *");
        System.out.println("* / /_/ / (__  ) / / /  __/ /_/ /  / ___ / /_/ /_/ /  / /_/ / /__/ /_/ / /_/ / / / (__  )  / /_/ / /  / /___/ / *");
        System.out.println("*/_____/_/____/_/ /_/\\___/\\__, /  /_/  |_\\__/\\__/_/   \\__,_/\\___/\\__/_/\\____/_/ /_/____/  /_____/_/  /_//____/  *");
        System.out.println("*                        /____/                                                                                 *");
        System.out.println("*****************************************************************************************************************");

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
        AttractionsTable attractionsTable = new AttractionsTable(attractionDatabase.getAllAttractions());
    }
}