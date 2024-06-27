/*
 * Phillip Yohner
 * CEN 3024C - 31950
 * June 27, 2024
 *
 * Class: UpdateMenu
 * This class defines the update menu GUI interface.
 * The user clicks a button corresponding with the attribute they wish to update.
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;

public class UpdateMenu extends JFrame {
    private int id;
    private JPanel updateMenuPanel;
    private JLabel lbTitle;
    private JLabel lbSelectedAttraction;
    private JButton btnUpdateName;
    private JButton btnUpdateDescription;
    private JButton btnUpdateLocation;
    private JButton btnUpdateType;
    private JButton btnUpdateHeight;
    private JButton btnUpdateThrill;
    private JButton btnUpdateOpeningDate;
    private JButton btnReturnToMain;
    private AttractionDatabase attractions;

    public UpdateMenu(AttractionDatabase attractions, int id) {
        setContentPane(updateMenuPanel);
        setTitle("Disney Attractions DMS");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        this.attractions = attractions;
        this.id = id;
        Attraction attraction = AttractionDatabase.getAttractionById(id);
        lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
        setVisible(true);

        btnUpdateName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the attraction name: ");
                attraction.setName(name);
                lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
            }
        });
        btnUpdateDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = JOptionPane.showInputDialog("Enter the description: ");
                attraction.setDescription(description);
                lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
            }
        });
        btnUpdateLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = JOptionPane.showInputDialog("Enter the location: ");
                attraction.setLocation(location);
                lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
            }
        });
        btnUpdateType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = JOptionPane.showInputDialog("Enter the type: ");
                attraction.setType(type);
                lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
            }
        });
        btnUpdateHeight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String height = JOptionPane.showInputDialog("Enter the height restriction: ");
                attraction.setHeight(height);
                lbSelectedAttraction.setText("<html><body style='width: 300px;'>"+ attraction.toString() +"</body></html>");
            }
        });
        btnUpdateThrill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int thrill = Integer.parseInt(JOptionPane.showInputDialog("Enter the thrill level (0-5): "));
                    if (thrill < 0 || thrill > 5) {
                        JOptionPane.showMessageDialog(new JOptionPane(), "Invalid thrill level.");
                    } else {
                        attraction.setThrill(thrill);
                        lbSelectedAttraction.setText("<html><body style='width: 300px;'>" + attraction.toString() + "</body></html>");
                    }
                } catch (NumberFormatException n){
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                } catch (InputMismatchException i) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnUpdateOpeningDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate openingDate = LocalDate.parse(JOptionPane.showInputDialog("Enter the opening date (yyyy-mm-dd): "));
                    attraction.setOpeningDate(openingDate);
                    lbSelectedAttraction.setText("<html><body style='width: 300px;'>" + attraction.toString() + "</body></html>");
                } catch (DateTimeException d){
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                } catch (InputMismatchException i) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Invalid entry.", "Invalid", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnReturnToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}