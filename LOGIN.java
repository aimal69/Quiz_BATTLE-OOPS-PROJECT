import  javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LOGIN extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, exitButton, createAccountButton;

    Connection conn;

    public LOGIN() {
        setTitle("Quiz Battle - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Quiz Battle Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        createAccountButton = new JButton("Create Account");

        loginButton.addActionListener(this);
        exitButton.addActionListener(this);
        createAccountButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(createAccountButton);
        panel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        connectDatabase();
        setVisible(true);
    }

    private void connectDatabase() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_game", "root", "Islamian0903");
            System.out.println("Connected to database successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            loginUser();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        } else if (e.getSource() == createAccountButton) {
            createAccount();
        }
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + username);
                this.dispose(); // Close login window
                SwingUtilities.invokeLater(() -> new Quiz_Battle(username)); // Open Quiz Battle with username
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking login: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAccount() {
        String username = JOptionPane.showInputDialog(this, "Enter a new username:");
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter a password:");
        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account.");
            }

            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LOGIN();
    }
}