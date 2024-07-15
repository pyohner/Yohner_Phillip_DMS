import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * AttractionTableModel ---
 * This class defines the table columns in the AttractionTable.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created July 2, 2024
 */
public class AttractionTableModel extends JFrame {

    private AttractionDatabase attractions;

    /**
     * DefaultTableModel --- takes in a list of attractions and places the data in rows.
     * Column names are set and the table model is returned.
     * @param attractions The attractions database
     * @return Returns the table model
     */
    public static DefaultTableModel buildTableModel(List<Attraction> attractions) {
        String[] columnNames = {"ID", "Name", "Description", "Location", "Type", "Height Restriction", "Thrill Level", "Opening Date", "Avg Rating"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Attraction attraction : attractions) {
            Object[] row = {
                    attraction.getId(),
                    attraction.getName(),
                    attraction.getDescription(),
                    attraction.getLocation(),
                    attraction.getType(),
                    attraction.getHeight(),
                    attraction.getThrill(),
                    attraction.getOpeningDate(),
                    attraction.getAverageRating()
            };
            model.addRow(row);
        }
        return model;
    }
}