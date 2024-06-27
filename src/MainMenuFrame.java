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

public class MainMenuFrame extends JFrame {
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
    private JButton btnLoadAttractionsFromFile;
    private JLabel lbAttractionCount;


    public MainMenuFrame(AttractionDatabase attractionDatabase){
        setContentPane(mainMenuPanel);
        setTitle("Disney Attractions DMS");
        setSize(450,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        getContentPane().setBackground(Color.white);
        setVisible(true);
        this.attractionDatabase = attractionDatabase;

        lbAttractionCount.setText(AttractionDatabase.listSize + " attractions loaded");

        btnAddAttraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attractionDatabase.addAttractionManually();
                lbAttractionCount.setText(AttractionDatabase.listSize + " attractions loaded");
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
                    lbAttractionCount.setText(AttractionDatabase.listSize + " attractions loaded");
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
                JTextArea listArea = new JTextArea(51, 100);
                listArea.setText(attractionDatabase.viewAttractions());
                JScrollPane scrollPane = new JScrollPane(listArea);
               JOptionPane.showMessageDialog(null, scrollPane, "List of all attractions", JOptionPane.INFORMATION_MESSAGE);
                
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
                    lbAttractionCount.setText(AttractionDatabase.listSize + " attractions loaded");
            }
        });
        btnLoadAttractionsFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String filePath = JOptionPane.showInputDialog("Enter the path of the text (.txt) file: ");
                    attractionDatabase.addAttractionsFromFile(filePath);
                    lbAttractionCount.setText(AttractionDatabase.listSize + " attractions loaded");
            }
        });
    }
}