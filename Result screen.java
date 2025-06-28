import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ResultsScreen extends JFrame {
    private String playerName, subject, topScore;
    private int score, totalQuestions;
    private boolean isNewHighScore;

    public ResultsScreen(String playerName, String subject, int score, int totalQuestions, String topScore) {
        this.playerName = playerName;
        this.subject = subject;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.topScore = topScore;

        // Check if this is a new high score
        this.isNewHighScore = checkIfNewHighScore();

        initializeUI();
    }

    private boolean checkIfNewHighScore() {
        // Check if the current player achieved the highest score
        if (topScore.contains(playerName)) {
            // Extract the score from the topScore string
            try {
                String[] parts = topScore.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("Score:")) {
                        int highestScore = Integer.parseInt(parts[i + 1]);
                        return score >= highestScore;
                    }
                }
            } catch (Exception e) {
                // If parsing fails, check if the score is mentioned in the string
                return topScore.contains(String.valueOf(score));
            }
        }
        return false;
    }
    
    private void initializeUI() {
        setTitle("Quiz Battle - Results");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with light blue gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Light blue gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255),  // Alice blue
                    getWidth(), getHeight(), new Color(220, 240, 255)  // Light blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern
                g2d.setColor(new Color(200, 230, 255, 30));
                for (int i = 0; i < getWidth(); i += 80) {
                    for (int j = 0; j < getHeight(); j += 80) {
                        g2d.fillOval(i, j, 3, 3);
                    }
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel with results
        JPanel centerPanel = createResultsPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(35, 20, 25, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Title with conditional text based on high score
        String titleText = isNewHighScore ? "NEW HIGH SCORE!" : "QUIZ COMPLETED!";
        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(isNewHighScore ? new Color(255, 215, 0) : new Color(25, 50, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add crown symbol for high score
        if (isNewHighScore) {
            JLabel crownLabel = new JLabel("★ CHAMPION ★", SwingConstants.CENTER);
            crownLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            crownLabel.setForeground(new Color(255, 215, 0));
            crownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(crownLabel);
            headerPanel.add(Box.createVerticalStrut(10));
        }

        // Subject
        JLabel subjectLabel = new JLabel(subject + " Quiz", SwingConstants.CENTER);
        subjectLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        subjectLabel.setForeground(new Color(50, 120, 200));
        subjectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(12));
        headerPanel.add(subjectLabel);

        return headerPanel;
    }
    
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setOpaque(false);
        resultsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        
        // Results container with light blue theme
        JPanel resultsContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Light blue gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 250),
                    0, getHeight(), new Color(240, 248, 255, 240)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Light blue border
                g2d.setColor(new Color(100, 180, 255, 100));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);

                g2d.dispose();
            }
        };
        resultsContainer.setOpaque(false);
        resultsContainer.setBorder(new EmptyBorder(35, 35, 35, 35));
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        
        // Player name
        JLabel playerLabel = new JLabel("Player: " + playerName, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        playerLabel.setForeground(new Color(25, 50, 100));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score display
        double percentage = (double) score / totalQuestions * 100;
        JLabel scoreLabel = new JLabel("Score: " + score + "/" + totalQuestions, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        scoreLabel.setForeground(getScoreColor(percentage));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Percentage
        JLabel percentageLabel = new JLabel(String.format("%.1f%%", percentage), SwingConstants.CENTER);
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        percentageLabel.setForeground(getScoreColor(percentage));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Performance message
        JLabel performanceLabel = new JLabel(getPerformanceMessage(percentage), SwingConstants.CENTER);
        performanceLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        performanceLabel.setForeground(new Color(100, 100, 100));
        performanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Progress bar with light blue theme
        JProgressBar progressBar = new JProgressBar(0, totalQuestions) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(200, 230, 255, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Progress fill
                int progressWidth = (int) ((double) getValue() / getMaximum() * getWidth());
                GradientPaint progressGradient = new GradientPaint(
                    0, 0, new Color(100, 180, 255),
                    progressWidth, 0, new Color(50, 150, 255)
                );
                g2d.setPaint(progressGradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 15, 15);

                g2d.dispose();
            }
        };
        progressBar.setValue(score);
        progressBar.setStringPainted(true);
        progressBar.setString(score + "/" + totalQuestions);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        progressBar.setPreferredSize(new Dimension(350, 35));
        progressBar.setMaximumSize(new Dimension(350, 35));
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        // Enhanced High Score Section
        JPanel highScorePanel = createHighScorePanel();
        
        // Add components
        resultsContainer.add(playerLabel);
        resultsContainer.add(Box.createVerticalStrut(25));
        resultsContainer.add(scoreLabel);
        resultsContainer.add(Box.createVerticalStrut(8));
        resultsContainer.add(percentageLabel);
        resultsContainer.add(Box.createVerticalStrut(15));
        resultsContainer.add(performanceLabel);
        resultsContainer.add(Box.createVerticalStrut(25));
        resultsContainer.add(progressBar);
        resultsContainer.add(Box.createVerticalStrut(30));
        resultsContainer.add(highScorePanel);

        resultsPanel.add(resultsContainer);
        return resultsPanel;
    }

    private JPanel createHighScorePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Golden background for high score
                Color bgColor = isNewHighScore ?
                    new Color(255, 215, 0, 150) :
                    new Color(100, 180, 255, 100);

                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Border
                g2d.setColor(isNewHighScore ?
                    new Color(255, 215, 0, 200) :
                    new Color(100, 180, 255, 150));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // High Score Title
        JLabel titleLabel = new JLabel("HIGHEST SCORE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(isNewHighScore ?
            new Color(184, 134, 11) :
            new Color(25, 50, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // High Score Details
        JLabel scoreLabel = new JLabel(topScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scoreLabel.setForeground(isNewHighScore ?
            new Color(146, 64, 14) :
            new Color(25, 50, 100));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Special message for new high score
        if (isNewHighScore) {
            JLabel congratsLabel = new JLabel("*** CONGRATULATIONS! ***", SwingConstants.CENTER);
            congratsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            congratsLabel.setForeground(new Color(184, 134, 11));
            congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(congratsLabel);
            panel.add(Box.createVerticalStrut(8));
        }

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scoreLabel);

        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton playAgainBtn = new JButton("Play Again");
        styleButton(playAgainBtn, new Color(50, 150, 255), new Color(30, 120, 220));
        playAgainBtn.addActionListener(e -> {
            dispose();
            new SubjectSelection(playerName);
        });

        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn, new Color(150, 170, 190), new Color(120, 140, 160));
        exitBtn.addActionListener(e -> System.exit(0));
        
        footerPanel.add(playAgainBtn);
        footerPanel.add(Box.createHorizontalStrut(20));
        footerPanel.add(exitBtn);
        
        return footerPanel;
    }
    
    private Color getScoreColor(double percentage) {
        if (percentage >= 80) return new Color(46, 204, 113); // Green
        else if (percentage >= 60) return new Color(241, 196, 15); // Yellow
        else if (percentage >= 40) return new Color(230, 126, 34); // Orange
        else return new Color(231, 76, 60); // Red
    }
    
    private String getPerformanceMessage(double percentage) {
        if (percentage >= 90) return "Outstanding! You're a quiz master!";
        else if (percentage >= 80) return "Excellent work! Great knowledge!";
        else if (percentage >= 70) return "Good job! Keep it up!";
        else if (percentage >= 60) return "Not bad! Room for improvement.";
        else if (percentage >= 50) return "You can do better! Try again.";
        else return "Keep studying and try again!";
    }
    
    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(150, 45));
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
}
