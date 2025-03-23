package IBCriterionC;

// Main class
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SportsGame extends JFrame {
    // Main reference for game panels
    private MainMenuPanel mainMenu;
    private BasketballGame basketballGame;
    private GolfGame golfGame;
    private SnakeGame snakeGame;
    private JPanel currentPanel;

    public SportsGame() {
        setTitle("Sports Mini-Games");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);

        // Initialize game panels
        mainMenu = new MainMenuPanel(this);
        basketballGame = new BasketballGame(this);
        golfGame = new GolfGame(this);
        snakeGame = new SnakeGame(this);

        // Set initial panel to main menu
        currentPanel = mainMenu;
        add(currentPanel);

        setVisible(true);
    }

    // Method to switch between game panels
    public void switchPanel(JPanel newPanel) {
        remove(currentPanel);
        currentPanel = newPanel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SportsGame());
    }
}

// Main Menu Panel
class MainMenuPanel extends JPanel {
    private SportsGame mainGame;
    private Image backgroundImage;

    public MainMenuPanel(SportsGame mainGame) {
        this.mainGame = mainGame;
        setLayout(null);

        // Load background image
        backgroundImage = new ImageIcon("resources/background.png").getImage();
        // Use placeholder if image not found
        if (backgroundImage.getWidth(null) <= 0) {
            backgroundImage = null;
        }

        // Basketball button
        JButton basketballBtn = createGameButton("Basketball", 100, 150);
        basketballBtn.addActionListener(e -> mainGame.switchPanel(mainGame.getComponent(1)));

        // Golf button
        JButton golfBtn = createGameButton("Golf", 325, 150);
        golfBtn.addActionListener(e -> mainGame.switchPanel(mainGame.getComponent(2)));

        // Snake button
        JButton snakeBtn = createGameButton("Snake", 550, 150);
        snakeBtn.addActionListener(e -> mainGame.switchPanel(mainGame.getComponent(3)));

        // Add title label
        JLabel titleLabel = new JLabel("Sports Mini-Games");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(225, 50, 400, 50);
        add(titleLabel);
    }

    private JButton createGameButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 150);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(245, 222, 179));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(40, 40, 60));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
