import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Quiz_Battle extends JFrame {

    private JLabel questionLabel, timerLabel, progressLabel;
    private JRadioButton aBtn, bBtn, cBtn, dBtn;
    private ButtonGroup group;
    private JButton nextBtn;
    private List<Question> questions;
    private int index = 0, score = 0;
    private String playerName, subject;
    private DatabaseManager db;
    private Timer questionTimer;
    private int timeLeft = 15;
    private JProgressBar progressBar;

   public Quiz_Battle(String username, String subject) {
        this.playerName = username;
        this.subject = subject;

        db = new DatabaseManager();
        setTitle("Quiz Battle - " + subject);
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create professional gradient background
        JPanel mainPanel = new ProfessionalGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Top Panel with enhanced header
        JPanel topPanel = createProfessionalTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize quiz
        initializeQuiz();

        setVisible(true);
    }

    private JPanel createProfessionalTopPanel() {
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Light blue header background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 200),
                    0, getHeight(), new Color(230, 245, 255, 180)
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
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Left side - Enhanced player info
        JPanel leftPanel = createPlayerInfoPanel();

        // Center - Professional progress section
        JPanel centerPanel = createProgressSection();

        // Right side - Simple timer
        JPanel rightPanel = createSimpleTimerPanel();

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel playerLabel = new JLabel(playerName);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        playerLabel.setForeground(new Color(25, 50, 100));  // Dark blue
        playerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subjectLabel = new JLabel(subject + " Quiz");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subjectLabel.setForeground(new Color(50, 120, 200));  // Medium blue
        subjectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(playerLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subjectLabel);

        return panel;
    }

    private JPanel createProgressSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));

        progressLabel = new JLabel("Question 1 of 5", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        progressLabel.setForeground(new Color(25, 50, 100));  // Dark blue

        // Custom light blue progress bar
        progressBar = new JProgressBar(0, 5) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(200, 230, 255, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Progress fill with blue gradient
                int progressWidth = (int) ((double) getValue() / getMaximum() * getWidth());
                GradientPaint progressGradient = new GradientPaint(
                    0, 0, new Color(100, 180, 255),
                    progressWidth, 0, new Color(50, 150, 255)
                );
                g2d.setPaint(progressGradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 12, 12);

                g2d.dispose();
            }
        };
        progressBar.setValue(1);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(350, 10));
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        panel.add(progressLabel, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(12), BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSimpleTimerPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel timerTitle = new JLabel("TIME LEFT");
        timerTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timerTitle.setForeground(new Color(50, 120, 200));
        timerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Simple rectangular timer display
        timerLabel = new JLabel("5s") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Rounded rectangle background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Border
                g2d.setColor(new Color(100, 180, 255, 150));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        timerLabel.setForeground(new Color(25, 50, 100));
        timerLabel.setBackground(new Color(230, 245, 255, 200));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel.setPreferredSize(new Dimension(80, 50));
        timerLabel.setOpaque(false);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(timerTitle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(timerLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Professional question card
        JPanel questionCard = createProfessionalQuestionCard();
        centerPanel.add(questionCard, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createProfessionalQuestionCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow
                g2d.setColor(new Color(100, 180, 255, 30));
                g2d.fillRoundRect(6, 6, getWidth()-6, getHeight()-6, 30, 30);

                // Card background with light blue theme
                GradientPaint cardGradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 250),
                    0, getHeight(), new Color(245, 250, 255, 240)
                );
                g2d.setPaint(cardGradient);
                g2d.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 30, 30);

                // Card border
                g2d.setColor(new Color(100, 180, 255, 80));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(2, 2, getWidth()-10, getHeight()-10, 30, 30);

                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Question header
        JPanel headerPanel = createQuestionHeader();
        card.add(headerPanel, BorderLayout.NORTH);

        // Question content
        JPanel contentPanel = createQuestionContent();
        card.add(contentPanel, BorderLayout.CENTER);

        // Options panel
        JPanel optionsPanel = createProfessionalOptionsPanel();
        card.add(optionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createQuestionHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 35, 0));

        // Question number with clear badge
        JLabel questionNumber = new JLabel("01") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Badge background with blue gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(50, 150, 255),
                    getWidth(), getHeight(), new Color(30, 120, 220)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        questionNumber.setFont(new Font("Segoe UI", Font.BOLD, 18));
        questionNumber.setForeground(Color.WHITE);
        questionNumber.setHorizontalAlignment(SwingConstants.CENTER);
        questionNumber.setVerticalAlignment(SwingConstants.CENTER);
        questionNumber.setPreferredSize(new Dimension(70, 40));
        questionNumber.setOpaque(false);
        questionNumber.putClientProperty("questionNumber", true);

        // Question category
        JLabel categoryLabel = new JLabel(subject.toUpperCase());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryLabel.setForeground(new Color(50, 120, 200));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(questionNumber);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(categoryLabel);

        header.add(leftPanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createQuestionContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 0, 45, 0));

        questionLabel = new JLabel("Loading question...", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        questionLabel.setForeground(new Color(25, 50, 100));  // Dark blue for better readability
        questionLabel.setBorder(new EmptyBorder(25, 0, 25, 0));

        content.add(questionLabel, BorderLayout.CENTER);
        return content;
    }

    private JPanel createProfessionalOptionsPanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new GridLayout(2, 2, 20, 15));

        // Create premium option buttons
        aBtn = createPremiumOptionButton("A");
        bBtn = createPremiumOptionButton("B");
        cBtn = createPremiumOptionButton("C");
        dBtn = createPremiumOptionButton("D");

        group = new ButtonGroup();
        group.add(aBtn);
        group.add(bBtn);
        group.add(cBtn);
        group.add(dBtn);

        optionsPanel.add(aBtn);
        optionsPanel.add(bBtn);
        optionsPanel.add(cBtn);
        optionsPanel.add(dBtn);

        return optionsPanel;
    }

    private JRadioButton createPremiumOptionButton(String optionLetter) {
        JRadioButton btn = new JRadioButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button shadow
                g2d.setColor(new Color(100, 180, 255, 25));
                g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 20, 20);

                // Button background
                if (isSelected()) {
                    GradientPaint selectedGradient = new GradientPaint(
                        0, 0, new Color(50, 150, 255),
                        0, getHeight(), new Color(30, 120, 220)
                    );
                    g2d.setPaint(selectedGradient);
                } else if (getModel().isRollover()) {
                    GradientPaint hoverGradient = new GradientPaint(
                        0, 0, new Color(240, 248, 255),
                        0, getHeight(), new Color(230, 245, 255)
                    );
                    g2d.setPaint(hoverGradient);
                } else {
                    GradientPaint normalGradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, getHeight(), new Color(248, 252, 255)
                    );
                    g2d.setPaint(normalGradient);
                }

                g2d.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 20, 20);

                // Button border
                if (isSelected()) {
                    g2d.setColor(new Color(30, 120, 220));
                    g2d.setStroke(new BasicStroke(3f));
                } else {
                    g2d.setColor(new Color(150, 200, 255, 150));
                    g2d.setStroke(new BasicStroke(2f));
                }
                g2d.drawRoundRect(2, 2, getWidth()-8, getHeight()-8, 20, 20);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setForeground(new Color(25, 50, 100));  // Dark blue for better readability
        btn.setBorder(new EmptyBorder(15, 18, 15, 18));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(350, 65));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enhanced interaction effects
        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setForeground(Color.WHITE);
            } else {
                btn.setForeground(new Color(25, 50, 100));
            }
            btn.repaint();
        });

        btn.addActionListener(e -> {
            nextBtn.setEnabled(true);
            // Add subtle animation effect
            Timer animationTimer = new Timer(50, null);
            animationTimer.addActionListener(animEvent -> {
                btn.repaint();
                animationTimer.stop();
            });
            animationTimer.start();
        });

        return btn;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 25, 30, 25));

        // Professional action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setOpaque(false);

        // Skip button with professional styling
        JButton skipBtn = createProfessionalButton("SKIP", new Color(149, 165, 166), new Color(127, 140, 141), false);
        skipBtn.addActionListener(e -> nextQuestion());

        // Next button with premium styling
        nextBtn = createProfessionalButton("CONTINUE", new Color(100, 200, 255), new Color(50, 150, 255), true);
        nextBtn.setEnabled(false);
        nextBtn.addActionListener(e -> nextQuestion());

        actionPanel.add(skipBtn);
        actionPanel.add(nextBtn);

        bottomPanel.add(actionPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private JButton createProfessionalButton(String text, Color bgColor, Color hoverColor, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button shadow
                if (isPrimary) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 25, 25);
                }

                // Button background
                if (isEnabled()) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, getBackground(),
                        0, getHeight(), getBackground().darker()
                    );
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(new Color(200, 200, 200));
                }

                g2d.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 25, 25);

                // Button border for secondary buttons
                if (!isPrimary) {
                    g2d.setColor(new Color(220, 220, 220));
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawRoundRect(1, 1, getWidth()-5, getHeight()-5, 25, 25);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(isPrimary ? Color.WHITE : new Color(100, 100, 100));
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enhanced hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.repaint();
            }
        });

        return button;
    }



    private void initializeQuiz() {
        questions = db.getQuestionsBySubject(subject);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions found for " + subject + " subject.");
            dispose();
            return;
        }

        // Start the first question
        showQuestion();
    }

    private void showQuestion() {
        if (index >= questions.size()) {
            endQuiz();
            return;
        }

        Question q = questions.get(index);

        // Update question number badge
        updateQuestionNumber(index + 1);

        // Set question text with better formatting
        String questionText = "<html><div style='text-align: center; line-height: 1.4; font-family: Segoe UI;'>" +
                             q.question + "</div></html>";
        questionLabel.setText(questionText);

        // Set option texts with modern formatting
        setOptionText(aBtn, "A", q.optionA);
        setOptionText(bBtn, "B", q.optionB);
        setOptionText(cBtn, "C", q.optionC);
        setOptionText(dBtn, "D", q.optionD);

        // Clear selection and reset button states
        group.clearSelection();
        resetOptionButtons();
        nextBtn.setEnabled(false);

        // Update progress
        progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
        progressBar.setValue(index + 1);
        progressBar.setString((index + 1) + "/" + questions.size());

        // Start timer with animation
        startTimer();
    }

    private void updateQuestionNumber(int questionNum) {
        // Find and update the question number badge
        Component[] components = getContentPane().getComponents();
        updateQuestionNumberRecursive(components, questionNum);
    }

    private void updateQuestionNumberRecursive(Component[] components, int questionNum) {
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getClientProperty("questionNumber") != null) {
                    label.setText(String.format("%02d", questionNum));
                    return;
                }
            } else if (comp instanceof Container) {
                updateQuestionNumberRecursive(((Container) comp).getComponents(), questionNum);
            }
        }
    }

    private void setOptionText(JRadioButton button, String letter, String text) {
        String formattedText = "<html><div style='padding: 10px; line-height: 1.5; font-family: Segoe UI;'>" +
                              "<span style='font-weight: bold; font-size: 18px; color: #1e3a8a; margin-right: 12px;'>" + letter + ".</span>" +
                              "<span style='color: #1e3a8a; font-size: 17px;'>" + text + "</span></div></html>";
        button.setText(formattedText);
    }

    private void resetOptionButtons() {
        JRadioButton[] buttons = {aBtn, bBtn, cBtn, dBtn};
        for (JRadioButton btn : buttons) {
            btn.setBackground(new Color(255, 255, 255, 240));
            btn.setBorder(new CompoundBorder(
                new RoundedBorder(12),
                new EmptyBorder(15, 20, 15, 20)
            ));
        }
    }

    private void startTimer() {
        timeLeft = 15;
        updateTimerDisplay();

        if (questionTimer != null) {
            questionTimer.stop();
        }

        questionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                updateTimerDisplay();

                if (timeLeft <= 0) {
                    questionTimer.stop();
                    // Auto-move to next question when time runs out
                    nextQuestion();
                }
            }
        });

        questionTimer.start();
        nextBtn.setEnabled(true);
    }

    private void updateTimerDisplay() {
        timerLabel.setText(timeLeft + "s");

        // Light blue theme color transitions
        Color timerColor;
        Color bgColor;

        if (timeLeft <= 1) {
            timerColor = new Color(220, 50, 50);  // Red
            bgColor = new Color(255, 230, 230, 200);
            // Pulsing effect for urgency
            Timer pulseTimer = new Timer(300, e -> {
                timerLabel.setForeground(timerLabel.getForeground().equals(timerColor) ?
                    new Color(180, 30, 30) : timerColor);
                timerLabel.repaint();
            });
            pulseTimer.setRepeats(false);
            pulseTimer.start();
        } else if (timeLeft <= 2) {
            timerColor = new Color(200, 120, 50);  // Orange
            bgColor = new Color(255, 245, 230, 200);
        } else if (timeLeft <= 3) {
            timerColor = new Color(180, 150, 50);  // Yellow
            bgColor = new Color(255, 250, 230, 200);
        } else {
            timerColor = new Color(25, 50, 100);  // Dark blue
            bgColor = new Color(230, 245, 255, 200);
        }

        timerLabel.setForeground(timerColor);
        timerLabel.setBackground(bgColor);
        timerLabel.repaint();
    }

    private void nextQuestion() {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        // Check answer
        String selected = getSelected();
        if (selected != null && selected.charAt(0) == questions.get(index).correctOption) {
            score++;
        }

        index++;

        if (index < questions.size()) {
            showQuestion();
        } else {
            endQuiz();
        }
    }

    private void endQuiz() {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        // Save score with subject information
        db.saveScoreWithSubject(playerName, score, subject);
        String topScore = db.getHighestScore();

        // Show results with enhanced high score display
        dispose();
        SwingUtilities.invokeLater(() -> new ResultsScreen(playerName, subject, score, questions.size(), topScore));
    }

    private String getSelected() {
        if (aBtn.isSelected()) return "A";
        if (bBtn.isSelected()) return "B";
        if (cBtn.isSelected()) return "C";
        if (dBtn.isSelected()) return "D";
        return null;
    }



    // Backward compatibility constructor
    public Quiz_Battle(String username) {
        this(username, "General");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LOGIN::new);
    }
}

// Light blue gradient background panel
class ProfessionalGradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create light blue gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(240, 248, 255),  // Alice blue
            getWidth(), getHeight(), new Color(220, 240, 255)  // Light blue
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add subtle geometric pattern
        g2d.setColor(new Color(200, 230, 255, 30));
        for (int i = 0; i < getWidth(); i += 80) {
            for (int j = 0; j < getHeight(); j += 80) {
                g2d.fillOval(i, j, 3, 3);
            }
        }
    }
}
