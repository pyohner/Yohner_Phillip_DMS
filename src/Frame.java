import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    private JPanel mainMenuPanel;
    private JButton btnAddAttraction;
    private JButton btnUpdateAttraction;
    private JButton btnRemoveAttractionById;
    private AttractionDatabase attractionDatabase;
    private JButton btnRateAttraction;
    private JButton btnViewTop10;
    private JButton btnViewAll;
    private JButton btnExit;
    private JLabel lbTitle;
    private JButton btnRemoveAttractionByNameLocation;



    public Frame(AttractionDatabase attractionDatabase){
        setContentPane(mainMenuPanel);
        setTitle("Disney Attractions DMS");
        setSize(450,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        setVisible(true);
        this.attractionDatabase = attractionDatabase;
        btnAddAttraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String name = JOptionPane.showInputDialog("Enter name: ");
//                String description = JOptionPane.showInputDialog("Enter description: ");
//                String location = JOptionPane.showInputDialog("Enter location: ");
//                String type = JOptionPane.showInputDialog("Enter type: ");
//                String height = JOptionPane.showInputDialog("Enter height restriction: ");
//                int thrill = Integer.parseInt(JOptionPane.showInputDialog("Enter thrill level: "));
//                LocalDate openingDate = LocalDate.parse(JOptionPane.showInputDialog("Enter opening date (yyyy-mm-dd): "));
//
//                attractionDatabase.addAttractionManually(name, description, location, type, height, thrill, openingDate );
                attractionDatabase.addAttractionManually();
            }
        });
        btnUpdateAttraction.addActionListener(new ActionListener() {
            int id;
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id = Integer.parseInt(JOptionPane.showInputDialog("Enter Attraction ID: "));
                    Attraction attraction = AttractionDatabase.getAttractionById(id);
                    if (attraction == null) { // If ID doesn't exist, let user know
                        JOptionPane.showMessageDialog(new JOptionPane(), "Attraction not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Attraction not found.");
                        return;
                    } else {
                        UpdateMenu updateMenu = new UpdateMenu(attractionDatabase, id);

                    }
//                    attractionDatabase.updateAttraction(id);
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                }


            }
        });
        btnRemoveAttractionById.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Attraction ID: "));
                    attractionDatabase.removeAttraction(id);
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnRateAttraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attractionDatabase.rateAttraction();
            }
        });
        btnViewTop10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(attractionDatabase,attractionDatabase.viewTopRatedAttractions(), "Top 10 Attractions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog(attractionDatabase, attractionDatabase.viewAttractions(), "List of all attractions", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnRemoveAttractionByNameLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog("Enter attraction name: ");
                    String location = JOptionPane.showInputDialog("Enter attraction location: ");
                    attractionDatabase.removeAttraction(name, location);
            }
        });
    }

}
