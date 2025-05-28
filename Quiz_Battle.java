
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class Quiz_Battle extends JFrame {
    
    private JTextField nameField;
    private JLabel questionLabel;
    private JRadioButton aBtn, bBtn, cBtn, dBtn;
    private ButtonGroup group;
    private JButton startBtn, nextBtn;
    private List<Question> questions;
    private int index = 0, score = 0;
    private String playerName;
    private DatabaseManager db;

   public Quiz_Battle(String username) {
    this.playerName = username;

        db = new DatabaseManager();
        setTitle("🎯 Quiz Battle");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Use BackgroundPanel
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);  // This line will work just fine now

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel("Enter Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        topPanel.add(nameLabel);
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(nameField);
        startBtn = new JButton("Start Quiz");
        styleButton(startBtn, new Color(0, 120, 215));
        topPanel.add(startBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 20, 20), new EtchedBorder()));

        questionLabel = new JLabel("Click Start to Begin");
        questionLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        centerPanel.add(questionLabel);

        aBtn = createOptionButton();
        bBtn = createOptionButton();
        cBtn = createOptionButton();
        dBtn = createOptionButton();
        group = new ButtonGroup();
        group.add(aBtn); group.add(bBtn); group.add(cBtn); group.add(dBtn);
        centerPanel.add(aBtn); centerPanel.add(bBtn);
        centerPanel.add(cBtn); centerPanel.add(dBtn);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        nextBtn = new JButton("Next");
        styleButton(nextBtn, new Color(34, 139, 34));
        nextBtn.setEnabled(false);
        bottomPanel.add(nextBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Start button event
        startBtn.addActionListener(_ -> {
            playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!");
                return;
            }
            questions = db.getQuestions();
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions found in database.");
                return;
            }
            startBtn.setEnabled(false);
            nameField.setEnabled(false);
            nextBtn.setEnabled(true);
            showQuestion();
        });

        // Next button event
        nextBtn.addActionListener(_ -> {
            String selected = getSelected();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select an option!");
                return;
            }
            if (selected.charAt(0) == questions.get(index).correctOption) score++;
            index++;
            if (index < questions.size()) {
                showQuestion();
            } else {
                db.saveScore(playerName, score);
                String topScore = db.getHighestScore();
                JOptionPane.showMessageDialog(this,
                        playerName + ", you scored: " + score + "\n" + topScore,
                        "Quiz Finished", JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void showQuestion() {
        Question q = questions.get(index);
        questionLabel.setText((index + 1) + ". " + q.question);
        aBtn.setText("A. " + q.optionA);
        bBtn.setText("B. " + q.optionB);
        cBtn.setText("C. " + q.optionC);
        dBtn.setText("D. " + q.optionD);
        group.clearSelection();
    }

    private String getSelected() {
        if (aBtn.isSelected()) return "A";
        if (bBtn.isSelected()) return "B";
        if (cBtn.isSelected()) return "C";
        if (dBtn.isSelected()) return "D";
        return null;
    }

    private JRadioButton createOptionButton() {
        JRadioButton btn = new JRadioButton();
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(255, 255, 255, 180)); // semi-transparent
        return btn;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 35));
    }
public static void main(String[] args) {
    SwingUtilities.invokeLater(LOGIN::new);
}
}

