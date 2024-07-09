/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * June 27, 2024
 *
 * Class: MainMenuFrame
 * This class defines the main menu GUI.
 * The user clicks a button corresponding with the activity.
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
//import java.io.File;

public class MainMenuFrame extends JFrame {
    private JPanel mainMenuPanel;
    private JButton btnAddAttraction;
    private JButton btnUpdateAttraction;
    private JButton btnRemoveAttractionById;
    private JButton btnRateAttraction;
    private JButton btnViewTop10;
    private JButton btnViewAll;
    private JButton btnExit;
    private JLabel lbTitle;
    private JButton btnRemoveAttractionByNameLocation;
//    private JButton btnLoadAttractionsFromFile;
    private JLabel lbAttractionCount;


    public MainMenuFrame(AttractionDatabase attractionDatabase){
        setContentPane(mainMenuPanel);
        setTitle("Disney Attractions DMS");
        setSize(450,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        getContentPane().setBackground(Color.white);
        setVisible(true);

        // Set Mnemonics ( Alt+<letter> to select )
        btnExit.setMnemonic('E');
//        btnLoadAttractionsFromFile.setMnemonic('L');
        btnAddAttraction.setMnemonic('A');
        btnUpdateAttraction.setMnemonic('U');
        btnRemoveAttractionById.setMnemonic('I');
        btnRemoveAttractionByNameLocation.setMnemonic('N');
        btnRateAttraction.setMnemonic('R');
        btnViewTop10.setMnemonic('T');
        btnViewAll.setMnemonic('V');

        lbAttractionCount.setText(attractionDatabase.getDatabaseSize() + " attractions loaded");

        btnAddAttraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attractionDatabase.addAttractionManually();
                lbAttractionCount.setText(attractionDatabase.getDatabaseSize() + " attractions loaded");
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
                    lbAttractionCount.setText(attractionDatabase.getDatabaseSize() + " attractions loaded");
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
                AttractionsTable attractionsTable = new AttractionsTable(attractionDatabase.getTopTen());
            }
        });

        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttractionsTable attractionsTable = new AttractionsTable(attractionDatabase.getAllAttractions());
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
                try {
                    attractionDatabase.removeAttraction(name, location);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                lbAttractionCount.setText(attractionDatabase.getDatabaseSize() + " attractions loaded");
            }
        });

//        btnLoadAttractionsFromFile.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                    String filePath = JOptionPane.showInputDialog("Enter the path of the text (.txt) file: ");
//                    File file = new File(filePath);
//                    if(!file.exists()){
//                        JOptionPane.showMessageDialog(new JOptionPane(), "File not found: " + filePath, "File Not Found", JOptionPane.INFORMATION_MESSAGE);
//                    } else {
//                        attractionDatabase.manualAttractionsFromFile(filePath);
//                        lbAttractionCount.setText(attractionDatabase.getDatabaseSize() + " attractions loaded");
//                    }
//            }
//        });
    }
}