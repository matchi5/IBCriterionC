package IBCriterionC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame();
        });
    }

    public SnakeGame() {
        add(new GamePanel());
        setTitle("Enhanced Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class GamePanel extends JPanel implements Runnable {
    // Game constants
    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final int GRID_SIZE = 20;
    private static final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / (GRID_SIZE * GRID_SIZE);
    private static final int DELAY = 100; // Game speed

    // Game colors
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    private static final Color SNAKE_COLOR = new Color(50, 205, 50);
    private static final Color SNAKE_HEAD_COLOR = new Color(34, 139, 34);
    private static final Color FOOD_COLOR = new Color(220, 20, 60);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    // Snake variables
    private final ArrayList<Point> snake = new ArrayList<>();
    private int direction = KeyEvent.VK_RIGHT; // Initial direction
    private int newDirection = KeyEvent.VK_RIGHT;

    // Game state variables
    private Point food;
    private int score = 0;
    private boolean isRunning = false;
    private boolean isGameOver = false;
    private Thread gameThread;
    private final Random random = new Random();

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);
        addKeyListener(new GameKeyAdapter());
        startGame();
    }

    private void startGame() {
        // Initialize snake with 3 segments
        snake.clear();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));

        direction = KeyEvent.VK_RIGHT;
        newDirection = KeyEvent.VK_RIGHT;
        score = 0;
        isGameOver = false;
        spawnFood();

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void spawnFood() {
        int x, y;
        boolean validPosition;

        do {
            validPosition = true;
            x = random.nextInt(PANEL_WIDTH / GRID_SIZE);
            y = random.nextInt(PANEL_HEIGHT / GRID_SIZE);

            // Make sure food doesn't spawn on snake
            for (Point segment : snake) {
                if (segment.x == x && segment.y == y) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);

        food = new Point(x, y);
    }

    private void move() {
        // Update direction
        direction = newDirection;

        // Get head position
        Point head = snake.get(0);
        Point newHead = new Point(head);

        // Calculate new head position based on direction
        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
        }

        // Check for game over conditions
        if (newHead.x < 0 || newHead.x >= PANEL_WIDTH / GRID_SIZE ||
                newHead.y < 0 || newHead.y >= PANEL_HEIGHT / GRID_SIZE ||
                checkCollision(newHead)) {
            isGameOver = true;
            isRunning = false;
            return;
        }

        // Add new head
        snake.add(0, newHead);

        // Check if snake eats food
        if (newHead.x == food.x && newHead.y == food.y) {
            score++;
            spawnFood();
        } else {
            // Remove tail if no food was eaten
            snake.remove(snake.size() - 1);
        }
    }

    private boolean checkCollision(Point head) {
        // Check if head collides with any part of the snake (except the tail that will be removed)
        for (int i = 0; i < snake.size() - 1; i++) {
            Point segment = snake.get(i);
            if (head.x == segment.x && head.y == segment.y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (isRunning) {
            // Draw grid lines (optional)
            g.setColor(new Color(20, 20, 20));
            for (int i = 0; i < PANEL_HEIGHT / GRID_SIZE; i++) {
                g.drawLine(0, i * GRID_SIZE, PANEL_WIDTH, i * GRID_SIZE);
                g.drawLine(i * GRID_SIZE, 0, i * GRID_SIZE, PANEL_HEIGHT);
            }

            // Draw food
            g.setColor(FOOD_COLOR);
            g.fillOval(food.x * GRID_SIZE, food.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                Point segment = snake.get(i);

                // Different color for head
                if (i == 0) {
                    g.setColor(SNAKE_HEAD_COLOR);
                } else {
                    g.setColor(SNAKE_COLOR);
                }

                g.fillRect(segment.x * GRID_SIZE, segment.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }

            // Draw score
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25);
        } else if (isGameOver) {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        // Game Over text
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        g.drawString(gameOverText,
                (PANEL_WIDTH - metrics.stringWidth(gameOverText)) / 2,
                PANEL_HEIGHT / 2 - 50);

        // Final score
        g.setFont(new Font("Arial", Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        String scoreText = "Score: " + score;
        g.drawString(scoreText,
                (PANEL_WIDTH - metrics.stringWidth(scoreText)) / 2,
                PANEL_HEIGHT / 2);

        // Restart instructions
        g.setFont(new Font("Arial", Font.BOLD, 20));
        metrics = getFontMetrics(g.getFont());
        String restartText = "Press SPACE to restart";
        g.drawString(restartText,
                (PANEL_WIDTH - metrics.stringWidth(restartText)) / 2,
                PANEL_HEIGHT / 2 + 50);
    }

    @Override
    public void run() {
        // Game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 1000.0 / DELAY;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                move();
                repaint();
                delta--;
            }

            // Small delay to reduce CPU usage
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (isRunning) {
                // Update direction, prevent 180-degree turns
                if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
                    newDirection = KeyEvent.VK_LEFT;
                } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                    newDirection = KeyEvent.VK_RIGHT;
                } else if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
                    newDirection = KeyEvent.VK_UP;
                } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
                    newDirection = KeyEvent.VK_DOWN;
                }
            } else if (isGameOver && key == KeyEvent.VK_SPACE) {
                // Restart game on SPACE after game over
                startGame();
            }
        }
    }
}
