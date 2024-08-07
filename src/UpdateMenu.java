import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;

/**
 * UpdateMenu ---
 * This class defines the update menu GUI interface.
 * The user clicks a button corresponding with the attribute they wish to update.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created June 27, 2024
 */
public class UpdateMenu extends JFrame {
    private int id;
    private JPanel updateMenuPanel;
    private JLabel lbTitle;
    private JButton btnUpdateName;
    private JButton btnUpdateDescription;
    private JButton btnUpdateLocation;
    private JButton btnUpdateType;
    private JButton btnUpdateHeight;
    private JButton btnUpdateThrill;
    private JButton btnUpdateOpeningDate;
    private JButton btnReturnToMain;
    private AttractionDatabase attractions;

    /**
     * Sets up the "Update Attraction" user menu
     * @param attractionDatabase The attractions database
     * @param id The attraction ID
     */
    public UpdateMenu(AttractionDatabase attractionDatabase, int id) {
        setContentPane(updateMenuPanel);
        setTitle("Disney Attractions DMS");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        this.attractions = attractionDatabase;
        this.id = id;
        final Attraction[] attraction = {AttractionDatabase.getAttractionById(id)};
        btnUpdateName.setText("<html><body >"+ "Name: " + attraction[0].getName().toString() +"</body></html>");
        btnUpdateDescription.setText("<html><body >"+ "Description: " + attraction[0].getDescription().toString() +"</body></html>");
        btnUpdateLocation.setText("<html><body >"+ "Location: " + attraction[0].getLocation().toString() +"</body></html>");
        btnUpdateType.setText("<html><body >"+ "Type: " + attraction[0].getType().toString() +"</body></html>");
        btnUpdateHeight.setText("<html><body >"+ "Height Restriction: " + attraction[0].getHeight().toString() +"</body></html>");
        btnUpdateThrill.setText("<html><body >"+ "Thrill Level: " + attraction[0].getThrill() +"</body></html>");
        btnUpdateOpeningDate.setText("<html><body >"+ "Opening Date: " + attraction[0].getOpeningDate().toString() +"</body></html>");
        setVisible(true);

        // Set Mnemonics ( Alt+<letter> to select )
        btnUpdateName.setMnemonic('N');
        btnUpdateDescription.setMnemonic('D');
        btnUpdateLocation.setMnemonic('L');
        btnUpdateType.setMnemonic('Y');
        btnUpdateHeight.setMnemonic('H');
        btnUpdateThrill.setMnemonic('T');
        btnUpdateOpeningDate.setMnemonic('O');
        btnReturnToMain.setMnemonic('E');

        btnUpdateName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter the attraction name: ");
                if (attractionDatabase.isUniqueAttraction(name, attraction[0].getLocation())) {
                    attractionDatabase.updateName(id, name);
                    attraction[0] = AttractionDatabase.getAttractionById(id);
                    btnUpdateName.setText("<html><body >"+ "Name: " + attraction[0].getName().toString() +"</body></html>");
                } else {
                    JOptionPane.showMessageDialog(new JOptionPane(), "This attraction already exists.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnUpdateDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = JOptionPane.showInputDialog("Enter the description: ");
                attractionDatabase.updateDescription(id, description);
                attraction[0] = AttractionDatabase.getAttractionById(id);
                btnUpdateDescription.setText("<html><body >"+ "Description: " + attraction[0].getDescription().toString() +"</body></html>");
            }
        });

        btnUpdateLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = JOptionPane.showInputDialog("Enter the location: ");
                if (attractionDatabase.isUniqueAttraction(attraction[0].getName(), location)) {
                    attractionDatabase.updateLocation(id, location);
                    attraction[0] = AttractionDatabase.getAttractionById(id);
                    btnUpdateLocation.setText("<html><body >"+ "Location: " + attraction[0].getLocation().toString() +"</body></html>");
                } else {
                    JOptionPane.showMessageDialog(new JOptionPane(), "This attraction already exists.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnUpdateType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = JOptionPane.showInputDialog("Enter the type: ");
                attractionDatabase.updateType(id, type);
                attraction[0] = AttractionDatabase.getAttractionById(id);
                btnUpdateType.setText("<html><body >"+ "Type: " + attraction[0].getType().toString() +"</body></html>");
            }
        });

        btnUpdateHeight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String height = JOptionPane.showInputDialog("Enter the height restriction: ");
                attractionDatabase.updateHeight(id, height);
                attraction[0] = AttractionDatabase.getAttractionById(id);
                btnUpdateHeight.setText("<html><body >"+ "Height Restriction: " + attraction[0].getHeight().toString() +"</body></html>");
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
                        attractionDatabase.updateThrill(id, thrill);
                        attraction[0] = AttractionDatabase.getAttractionById(id);
                        btnUpdateThrill.setText("<html><body >"+ "Thrill Level: " + attraction[0].getThrill() +"</body></html>");
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
                    attractionDatabase.updateOpeningDate(id, openingDate);
                    attraction[0] = AttractionDatabase.getAttractionById(id);
                    btnUpdateOpeningDate.setText("<html><body >"+ "Opening Date: " + attraction[0].getOpeningDate().toString() +"</body></html>");
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