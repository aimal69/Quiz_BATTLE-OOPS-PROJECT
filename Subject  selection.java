import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SubjectSelection extends JFrame {
    private String username;
    private JButton mathBtn, scienceBtn, historyBtn, geographyBtn, englishBtn, computerBtn;
    
    public SubjectSelection(String username) {
        this.username = username;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Quiz Battle - Select Subject");
        setSize(600, 700);
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
        
        // Center Panel with subject buttons
        JPanel centerPanel = createSubjectPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Instruction
        JLabel instructionLabel = new JLabel("Choose Your Quiz Subject", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        instructionLabel.setForeground(new Color(220, 220, 220));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(instructionLabel);
        
        return headerPanel;
    }
    
    private JPanel createSubjectPanel() {
        JPanel subjectPanel = new JPanel();
        subjectPanel.setOpaque(false);
        subjectPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        subjectPanel.setLayout(new GridLayout(3, 2, 20, 20));
        
        // Create subject buttons
        mathBtn = createSubjectButton("Mathematics", "Numbers & Logic", new Color(231, 76, 60));
        scienceBtn = createSubjectButton("Science", "Physics & Chemistry", new Color(52, 152, 219));
        historyBtn = createSubjectButton("History", "Past Events", new Color(155, 89, 182));
        geographyBtn = createSubjectButton("Geography", "World & Places", new Color(46, 204, 113));
        englishBtn = createSubjectButton("English", "Language & Literature", new Color(241, 196, 15));
        computerBtn = createSubjectButton("Computer", "Technology & Programming", new Color(230, 126, 34));
        
        // Add action listeners
        mathBtn.addActionListener(e -> startQuiz("Mathematics"));
        scienceBtn.addActionListener(e -> startQuiz("Science"));
        historyBtn.addActionListener(e -> startQuiz("History"));
        geographyBtn.addActionListener(e -> startQuiz("Geography"));
        englishBtn.addActionListener(e -> startQuiz("English"));
        computerBtn.addActionListener(e -> startQuiz("Computer"));
        
        subjectPanel.add(mathBtn);
        subjectPanel.add(scienceBtn);
        subjectPanel.add(historyBtn);
        subjectPanel.add(geographyBtn);
        subjectPanel.add(englishBtn);
        subjectPanel.add(computerBtn);
        
        return subjectPanel;
    }
    
    private JButton createSubjectButton(String subject, String description, Color color) {
        // Create a custom button that doesn't display any default text
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // Custom paint to ensure no default text is shown
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.dispose();

                // Paint children (our custom labels)
                super.paintChildren(g);
            }
        };

        button.setText(null);  // Ensure no text
        button.setLayout(new BorderLayout());
        button.setBackground(new Color(255, 255, 255, 240));
        button.setBorder(new RoundedBorder(15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 120));
        button.setContentAreaFilled(false);  // We'll paint our own background
        button.setBorderPainted(false);
        button.setOpaque(false);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 10, 15, 10));

        // Subject name
        JLabel nameLabel = new JLabel(subject, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(color);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalGlue());

        button.add(contentPanel, BorderLayout.CENTER);
        
        // Add hover effect
        Color originalBg = new Color(255, 255, 255, 240);
        Color hoverBg = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton backBtn = new JButton("Back to Login");
        styleButton(backBtn, new Color(149, 165, 166), new Color(127, 140, 141));
        backBtn.addActionListener(e -> {
            dispose();
            new LOGIN();
        });
        
        footerPanel.add(backBtn);
        return footerPanel;
    }
    
    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new RoundedBorder(8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void startQuiz(String subject) {
        JOptionPane.showMessageDialog(this, 
            "Starting " + subject + " Quiz!\nGet ready for 15 seconds per question!", 
            "Quiz Starting", 
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        SwingUtilities.invokeLater(() -> new Quiz_Battle(username, subject));
    }
}
