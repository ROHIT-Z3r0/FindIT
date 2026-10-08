import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewFoundItems extends JFrame {
    private MainFrame parentFrame;

    // Table
    private JTable table;
    // Table model
    private DefaultTableModel model;
    // Search field
    private JTextField searchField;

    public ViewFoundItems(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

        
        // window settings
        setTitle("View Found Items");
        setSize(1000, 550);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
        setLocationRelativeTo(null);

        
        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15)
        );
        mainPanel.setLayout(new BorderLayout(10, 10));

        
        // title
        JLabel titleLabel = new JLabel("FOUND ITEMS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        
        // SEARCH PANEL
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);


        JLabel searchLabel = new JLabel("Search Item:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("SEARCH");
        JButton refreshButton = new JButton("REFRESH");


        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        mainPanel.add(
                searchPanel,
                BorderLayout.SOUTH
        );
        
        // TABLE
        String[] columns = {
                "ID",
                "Item Name",
                "Color",
                "Location",
                "Date",
                "Founder Name",
                "Mail ID",
                "Status",
                "Matched Lost ID"

        };
        
        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font(
                                "Arial",
                                Font.BOLD,
                                13)
                );
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        
        // SEARCH BUTTON
        searchButton.addActionListener(e -> {
            searchFoundItems();
        });
        
        // REFRESH BUTTON
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadFoundItems();
        });
        
        // ENTER KEY SEARCH
        searchField.addActionListener(e -> {
            searchFoundItems();
        });
        
        // Add main panel
        add(mainPanel);

        // Load records
        loadFoundItems();

        // Show window
        setVisible(true);
    }


    // LOAD ALL FOUND ITEMS
    private void loadFoundItems() {
        String sql =
                "SELECT * FROM found_items " +
                        "ORDER BY found_id DESC";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Remove old rows
            model.setRowCount(0);


            // Read database rows
            while (rs.next()) {
                Object matchedId =
                        rs.getObject("matched_lost_id");

                model.addRow(new Object[]{
                        rs.getInt("found_id"),
                        rs.getString("item_name"),
                        rs.getString("color"),
                        rs.getString("location_found"),
                        rs.getDate("date_found"),
                        rs.getString("founder_name"),
                        rs.getString("mail_id"),
                        rs.getString("status"),
                        matchedId
                });
            }
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading found items:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // SEARCH FOUND ITEMS
    private void searchFoundItems() {
        String searchText =
                searchField.getText().trim();
        // Empty search = show everything
        if (searchText.isEmpty()) {
            loadFoundItems();
            return;
        }
        String sql =
                "SELECT * FROM found_items " +
                        "WHERE LOWER(item_name) LIKE LOWER(?) " +
                        "ORDER BY found_id DESC";

        try {

            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);

            // Partial search
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            // Clear old rows
            model.setRowCount(0);

            // Add matching rows
            while (rs.next()) {
                Object matchedId = rs.getObject("matched_lost_id");

                model.addRow(new Object[]{

                        rs.getInt("found_id"),
                        rs.getString("item_name"),
                        rs.getString("color"),
                        rs.getString("location_found"),
                        rs.getDate("date_found"),
                        rs.getString("founder_name"),
                        rs.getString("mail_id"),
                        rs.getString("status"),
                        matchedId
                });
            }
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error searching found items:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}