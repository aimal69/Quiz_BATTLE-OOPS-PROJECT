
import javax.swing.*;
import javax.swing.border.*;
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
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel with login form
        JPanel centerPanel = createLoginPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        connectDatabase();
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("QUIZ BATTLE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Test Your Knowledge", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        // Login form container
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 255, 230));
        formPanel.setBorder(new CompoundBorder(
            new RoundedBorder(20),
            new EmptyBorder(30, 30, 30, 30)
        ));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(51, 51, 51));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(51, 51, 51));

        usernameField = new JTextField();
        styleTextField(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(51, 51, 51));

        passwordField = new JPasswordField();
        styleTextField(passwordField);

        // Buttons
        loginButton = new JButton("LOGIN");
        styleButton(loginButton, new Color(52, 152, 219), new Color(41, 128, 185));

        createAccountButton = new JButton("CREATE ACCOUNT");
        styleButton(createAccountButton, new Color(46, 204, 113), new Color(39, 174, 96));

        // Add action listeners
        loginButton.addActionListener(this);
        createAccountButton.addActionListener(this);

        // Add components to form
        formPanel.add(welcomeLabel);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createAccountButton);

        loginPanel.add(formPanel);
        return loginPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        exitButton = new JButton("EXIT");
        styleButton(exitButton, new Color(231, 76, 60), new Color(192, 57, 43));
        exitButton.addActionListener(this);

        footerPanel.add(exitButton);
        return footerPanel;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setBorder(new CompoundBorder(
            new RoundedBorder(8),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(51, 51, 51));
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new RoundedBorder(8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void connectDatabase() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database - Updated to use quiz_database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "Islamian0903");
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
                SwingUtilities.invokeLater(() -> new SubjectSelection(username)); // Open Subject Selection
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
        SwingUtilities.invokeLater(() -> new LOGIN());
    }
}

// Custom gradient panel class
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Create gradient from top to bottom
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(74, 144, 226),
            0, getHeight(), new Color(80, 170, 200)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

// Custom rounded border class
class RoundedBorder implements Border {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius/2, radius/2, radius/2, radius/2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}
