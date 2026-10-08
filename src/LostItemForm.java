import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class LostItemForm extends JFrame {

    // Reference to the MainFrame
    private MainFrame parentFrame;

    // Text fields
    private JTextField itemNameField;
    private JTextField colorField;
    private JTextField locationField;
    private JTextField dateField;
    private JTextField userNameField;
    private JTextField departmentField;
    private JTextField registerNumberField;


    public LostItemForm(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

        // Window settings
        setTitle("Post Lost Item");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // title
        JLabel titleLabel = new JLabel("Ahh you lost something ?" +""+ SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(
                new GridLayout(7, 2, 10, 15)
        );


        // Labels
        JLabel itemNameLabel = new JLabel("Item Name:");
        JLabel colorLabel = new JLabel("Color:");
        JLabel locationLabel = new JLabel("Location Lost:");
        JLabel dateLabel = new JLabel("Date Lost (yyyy-mm-dd):");
        JLabel userNameLabel = new JLabel("User Name:");
        JLabel departmentLabel = new JLabel("Department:");
        JLabel registerNumberLabel = new JLabel("Register Number:");


        // Text fields

        itemNameField = new JTextField();
        colorField = new JTextField();
        locationField = new JTextField();
        dateField = new JTextField();
        userNameField = new JTextField();
        departmentField = new JTextField();
        registerNumberField = new JTextField();

        // Add components
        formPanel.add(itemNameLabel);
        formPanel.add(itemNameField);

        formPanel.add(colorLabel);
        formPanel.add(colorField);

        formPanel.add(locationLabel);
        formPanel.add(locationField);

        formPanel.add(dateLabel);
        formPanel.add(dateField);

        formPanel.add(userNameLabel);
        formPanel.add(userNameField);

        formPanel.add(departmentLabel);
        formPanel.add(departmentField);

        formPanel.add(registerNumberLabel);
        formPanel.add(registerNumberField);


        mainPanel.add(formPanel, BorderLayout.CENTER);

        
        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton submitButton = new JButton("SUBMIT");
        JButton cancelButton = new JButton("CANCEL");


        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        
        // SUBMIT BUTTON
        submitButton.addActionListener(e -> {
            submitLostItem();
        });

        
        // CANCEL BUTTON
        cancelButton.addActionListener(e -> {
            dispose();
        });


        // Add panel to frame
        add(mainPanel);

        // Show window
        setVisible(true);
    }


    
    // SUBMIT LOST ITEM
    private void submitLostItem() {

        // Get values
        String itemName = itemNameField.getText().trim();
        String color = colorField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String userName = userNameField.getText().trim();
        String department = departmentField.getText().trim();
        String registerNumber = registerNumberField.getText().trim();


        // VALIDATION
        if (itemName.isEmpty() ||
                color.isEmpty() ||
                location.isEmpty() ||
                date.isEmpty() ||
                userName.isEmpty() ||
                department.isEmpty() ||
                registerNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // insert
        String sql =
                "INSERT INTO lost_items " +
                        "(item_name, color, location_lost, date_lost, " +
                        "user_name, department, register_number) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, itemName);
            ps.setString(2, color);
            ps.setString(3, location);
            ps.setString(4, date);
            ps.setString(5, userName);
            ps.setString(6, department);
            ps.setString(7, registerNumber);
            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Lost item registered successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // Update dashboard counter
                parentFrame.updateCounts();


                // Close form
                dispose();
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving lost item:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

}