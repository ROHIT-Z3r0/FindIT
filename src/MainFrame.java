import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainFrame extends JFrame {//now MainFram is child class of
    // Jframe so no need to create a object for jframe

    // Labels for showing number of lost and found items
    private JLabel lostCountLabel;
    private JLabel foundCountLabel;


    public MainFrame() {

        setTitle("Lost and Found Management System");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Add header/title
        JLabel titleLabel = new JLabel(
                "LOST AND FOUND MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        //count pannel
        JPanel countPanel = new JPanel();
        countPanel.setBackground(Color.WHITE);
        countPanel.setLayout(new GridLayout(1, 2, 30, 10));

        lostCountLabel = new JLabel(
                "Items Lost: 0",
                SwingConstants.CENTER
        );

        foundCountLabel = new JLabel(
                "Items Found: 0",
                SwingConstants.CENTER
        );

        lostCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        foundCountLabel.setFont(new Font("Arial", Font.BOLD, 20));

        countPanel.add(lostCountLabel);
        countPanel.add(foundCountLabel);
        mainPanel.add(countPanel, BorderLayout.CENTER);

        //button
        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.setLayout(
                new GridLayout(3, 2, 20, 20));


        JButton lostButton = new JButton("POST LOST ITEM");
        JButton foundButton = new JButton("POST FOUND ITEM");
















































        JButton viewLostButton = new JButton("VIEW LOST ITEMS");
        JButton viewFoundButton = new JButton("VIEW FOUND ITEMS");

        JButton refreshButton = new JButton("REFRESH");
        JButton exitButton = new JButton("EXIT");


        // Add buttons to panel

        buttonPanel.add(lostButton);
        buttonPanel.add(foundButton);
        buttonPanel.add(viewLostButton);
        buttonPanel.add(viewFoundButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // button actions
        lostButton.addActionListener(e -> {
            new LostItemForm(this);
        });


        foundButton.addActionListener(e -> {
            new FoundItemForm(this);
        });


        viewLostButton.addActionListener(e -> {
            new ViewLostItems(this);
        });


        viewFoundButton.addActionListener(e -> {
            new ViewFoundItems(this);
        });


        refreshButton.addActionListener(e -> {
            updateCounts();
        });


        exitButton.addActionListener(e -> {
            System.exit(0);
        });


        // Add main panel to JFrame
        add(mainPanel);

        // Update database counts
        updateCounts();

        // Show window
        setVisible(true);
    }

    // count updates
    public void updateCounts() {

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();


            // Count lost items
            String lostQuery =
                    "SELECT COUNT(*) FROM lost_items";

            ResultSet lostResult =
                    stmt.executeQuery(lostQuery);

            if (lostResult.next()) {

                int lostCount =
                        lostResult.getInt(1);

                lostCountLabel.setText(
                        "Items Lost: " + lostCount
                );
            }


            // Count found items
            String foundQuery =
                    "SELECT COUNT(*) FROM found_items";

            ResultSet foundResult =
                    stmt.executeQuery(foundQuery);

            if (foundResult.next()) {

                int foundCount =
                        foundResult.getInt(1);

                foundCountLabel.setText(
                        "Items Found: " + foundCount
                );
            }


            con.close();

        } catch (Exception e) {

            System.out.println(
                    "Error updating counts"
            );

            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new MainFrame();
    }
}
