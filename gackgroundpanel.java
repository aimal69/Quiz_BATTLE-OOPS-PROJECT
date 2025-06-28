import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel {

    private Image backgroundImage;

    public BackGroundPanel() {
        // Load your background image here
        // You can put your image in the resources folder or same directory as the class file
        backgroundImage = new ImageIcon("background.jpg").getImage(); // Replace with your image file
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Scale image to panel size
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback background color
            setBackground(Color.DARK_GRAY);
        }
    }
}
