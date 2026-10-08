import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FoundItemForm extends JFrame {

    // Reference to MainFrame
    private MainFrame parentFrame;

    // Text fields
    private JTextField itemNameField;
    private JTextField colorField;
    private JTextField locationField;
    private JTextField dateField;
    private JTextField founderNameField;
    private JTextField mailIdField;


    // Variables to store matched lost item
    private int matchedLostId = -1;


    public FoundItemForm(MainFrame parentFrame) {

        this.parentFrame = parentFrame;

        // Window settings
        setTitle("Post Found Item");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        
        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                20, 30, 20, 30));

        mainPanel.setLayout(new BorderLayout(10, 10));


        
        // TITLE
        JLabel titleLabel = new JLabel("Great! You found something", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        
        // FORM PANEL
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(6, 2, 10, 15));


        // Labels
        JLabel itemNameLabel = new JLabel("Item Name:");
        JLabel colorLabel = new JLabel("Color:");
        JLabel locationLabel = new JLabel("Location Found:");
        JLabel dateLabel = new JLabel("Date Found:");
        JLabel founderNameLabel = new JLabel("Founder Name:");
        JLabel mailIdLabel = new JLabel("Mail ID:");


        // Text fields
        itemNameField = new JTextField();
        colorField = new JTextField();
        locationField = new JTextField();
        dateField = new JTextField();
        founderNameField = new JTextField();
        mailIdField = new JTextField();


        // Add components
        formPanel.add(itemNameLabel);
        formPanel.add(itemNameField);

        formPanel.add(colorLabel);
        formPanel.add(colorField);

        formPanel.add(locationLabel);
        formPanel.add(locationField);

        formPanel.add(dateLabel);
        formPanel.add(dateField);

        formPanel.add(founderNameLabel);
        formPanel.add(founderNameField);

        formPanel.add(mailIdLabel);
        formPanel.add(mailIdField);


        mainPanel.add(formPanel, BorderLayout.CENTER);

        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton checkButton = new JButton("CHECK");
        JButton cancelButton = new JButton("CANCEL");
        
        buttonPanel.add(checkButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        
        // CHECK BUTTON
        checkButton.addActionListener(e -> {
            checkForMatch();
        });

        
        // CANCEL BUTTON
        cancelButton.addActionListener(e -> {
            dispose();
        });
        
        // Add main panel
        add(mainPanel);

        // Show window
        setVisible(true);
    }

    
    // CHECK FOR MATCH
    private void checkForMatch() {
        // Get values

        String itemName = itemNameField.getText().trim();
        String color = colorField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String founderName = founderNameField.getText().trim();
        String mailId = mailIdField.getText().trim();
        
        // VALIDATION
        if (itemName.isEmpty() ||
                color.isEmpty() ||
                location.isEmpty() ||
                date.isEmpty() ||
                founderName.isEmpty() ||
                mailId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }
        
        // SEARCH DATABASE
        String sql =
                "SELECT * FROM lost_items " +
                        "WHERE LOWER(item_name) = LOWER(?) " +
                        "AND LOWER(color) = LOWER(?) " +
                        "AND status = 'Not Found' " +
                        "LIMIT 1";
        
        try {

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, itemName);
            ps.setString(2, color);
            ResultSet rs = ps.executeQuery();
            
            // MATCH FOUND
            if (rs.next()) {

                matchedLostId = rs.getInt("lost_id");
                String ownerName = rs.getString("user_name");
                String department = rs.getString("department");
                String registerNumber = rs.getString("register_number");
                String lostLocation = rs.getString("location_lost");
                String lostDate = rs.getString("date_lost");


                JOptionPane.showMessageDialog(
                        this,
                        "MATCH FOUND!\n\n" +
                                "Lost Item ID: " + matchedLostId + "\n" +
                                "Item: " + rs.getString("item_name") + "\n" +
                                "Color: " + rs.getString("color") + "\n\n" +
                                "Owner: " + ownerName + "\n" +
                                "Department: " + department + "\n" +
                                "Register Number: " + registerNumber + "\n\n" +
                                "Lost Location: " + lostLocation + "\n" +
                                "Lost Date: " + lostDate,
                        "Match Found",

                        JOptionPane.INFORMATION_MESSAGE
                );


                // check match
                int choice = JOptionPane.showConfirmDialog(
                                this,
                                "Do you want to register this " +
                                        "found item as a match?",
                                "Confirm Match",
                                JOptionPane.YES_NO_OPTION
                        );
                
                if (choice == JOptionPane.YES_OPTION) {
                    saveMatchedItem();
                }
                
            } 
            else {
                // NO MATCH
                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "No matching lost item found.\n\n" +
                                        "Do you want to register this " +
                                        "as a NEW FOUND item?",
                                "No Match",
                                JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    saveNewFoundItem();

                }
            }
            
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error checking for match:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
   
    // SAVE MATCHED ITEM
    private void saveMatchedItem() {

        String itemName = itemNameField.getText().trim();
        String color = colorField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String founderName = founderNameField.getText().trim();
        String mailId = mailIdField.getText().trim();


        String insertSql =
                "INSERT INTO found_items " +
                        "(item_name, color, location_found, date_found, " +
                        "founder_name, mail_id, status, matched_lost_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'Matched', ?)";
        
        String updateSql =
                "UPDATE lost_items " +
                        "SET status = 'Found' " +
                        "WHERE lost_id = ?";
        
        try {

            Connection con = DBConnection.getConnection();
            PreparedStatement insertps = con.prepareStatement(insertSql);

            insertps.setString(1, itemName);
            insertps.setString(2, color);
            insertps.setString(3, location);
            insertps.setString(4, date);
            insertps.setString(5, founderName);
            insertps.setString(6, mailId);
            insertps.setInt(7, matchedLostId);

            insertps.executeUpdate();
            PreparedStatement updateps = con.prepareStatement(updateSql);
            updateps.setInt(1, matchedLostId);


            updateps.executeUpdate();
            insertps.close();
            updateps.close();
            con.close();
   
            // SUCCESS
            JOptionPane.showMessageDialog(
                    this,
                    "Item successfully matched!\n\n" +
                            "Lost Item ID: " +
                            matchedLostId,
                    "Match Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
            
            // Update dashboard
            parentFrame.updateCounts();
            
            // Close form
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving matched item:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }


   
    // SAVE NEW FOUND ITEM
    private void saveNewFoundItem() {

        String itemName = itemNameField.getText().trim();
        String color = colorField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String founderName = founderNameField.getText().trim();
        String mailId = mailIdField.getText().trim();


        String sql =
                "INSERT INTO found_items " +
                        "(item_name, color, location_found, date_found, " +
                        "founder_name, mail_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'New Found')";


        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, itemName);
            ps.setString(2, color);
            ps.setString(3, location);
            ps.setString(4, date);
            ps.setString(5, founderName);
            ps.setString(6, mailId);

            ps.executeUpdate();
            ps.close();
            con.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Found item registered successfully!\n\n" +
                            "Status: New Found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // Update dashboard counter
            parentFrame.updateCounts();


            // Close window
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,

                    "Error saving found item:\n"
                            + e.getMessage(),

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}