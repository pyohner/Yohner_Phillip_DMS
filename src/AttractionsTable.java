import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.TableColumn;

/**
 * AttractionsTable ---
 * This class defines the attraction table view in the GUI interface.
 * The user clicks a close button when finished viewing the list.
 *
 * @author Phillip Yohner
 * @course CEN 3024C - 31950
 * @created July 2, 2024
 */
public class AttractionsTable extends JFrame {
    private JTable tblAttractionsTable;
    private JPanel tablePanel;
    private JButton btnClose;
    private JLabel lbTitle;
    private List<Attraction> attractions;

    /**
     * Sets up the table view of the attractions list
     * @param attractions The attractions list
     */
    public AttractionsTable(List<Attraction> attractions) {
        setContentPane(tablePanel);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        this.attractions = attractions;
        DefaultTableModel tableModel = AttractionTableModel.buildTableModel(attractions); // Call to AttractionTableModel
        tblAttractionsTable.setModel(tableModel);
        setColumnWidths(tblAttractionsTable, 10,200,20,20,20,20,20,20,10);
        setVisible(true);

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Sets the table column widths
     * @param table The table frame
     * @param widths Column widths
     */
    private void  setColumnWidths(JTable table, int... widths){
        for (int i = 0; i < widths.length; i++){
            if (i<table.getColumnModel().getColumnCount()){
                TableColumn column = table.getColumnModel().getColumn(i);
                column.setPreferredWidth(widths[i]);
            }
        }
    }
}