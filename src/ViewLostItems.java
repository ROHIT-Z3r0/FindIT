import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewLostItems extends JFrame {
    private MainFrame parentFrame;

    // Table
    private JTable table;
    private DefaultTableModel model;

    // Search field
    private JTextField searchField;
    
    public ViewLostItems(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

        // window setting
        setTitle("View Lost Items");
        setSize(1000, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                        15, 15, 15, 15));

        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // title
        JLabel titleLabel = new JLabel(
                "LOST ITEMS",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

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
                "User Name",
                "Department",
                "Register Number",
                "Status"

        };

        model = new DefaultTableModel(
                columns,
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // search panel
        searchButton.addActionListener(e -> {
            searchLostItems();
        });

        // refresh button
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadLostItems();

        });

        
        // key search
        searchField.addActionListener(e -> {
            searchLostItems();
        });

        add(mainPanel);

        // Load database records
        loadLostItems();

        // Show window
        setVisible(true);
    }

    //show items
    private void loadLostItems() {

        String sql =
                "SELECT * FROM lost_items " +
                        "ORDER BY lost_id DESC";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Remove old rows
            model.setRowCount(0);

            // Read database rows
            while (rs.next()) {

                model.addRow(new Object[]{

                        rs.getInt("lost_id"),
                        rs.getString("item_name"),
                        rs.getString("color"),
                        rs.getString("location_lost"),
                        rs.getDate("date_lost"),
                        rs.getString("user_name"),
                        rs.getString("department"),
                        rs.getString("register_number"),
                        rs.getString("status")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading lost items:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    // search lost
    private void searchLostItems() {
        String searchText =
                searchField.getText().trim();

        // If search box is empty,
        // show all items
        if (searchText.isEmpty()) {
            loadLostItems();
            return;
        }

        String sql =
                "SELECT * FROM lost_items " +
                        "WHERE LOWER(item_name) LIKE LOWER(?) " +
                        "ORDER BY lost_id DESC";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);

            // Add % for partial search
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            // Clear old rows
            model.setRowCount(0);
            // Add matching rows
            while (rs.next()) {
                model.addRow(new Object[]{

                        rs.getInt("lost_id"),
                        rs.getString("item_name"),
                        rs.getString("color"),
                        rs.getString("location_lost"),
                        rs.getDate("date_lost"),
                        rs.getString("user_name"),
                        rs.getString("department"),
                        rs.getString("register_number"),
                        rs.getString("status")
                });
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error searching lost items:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}