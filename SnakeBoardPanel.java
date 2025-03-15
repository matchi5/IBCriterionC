package hillcrest.project.snake;

import hillcrest.project.ending.EndingApp;
import hillcrest.project.ending.GameEndController;
import hillcrest.project.hub.HubApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Creates a board panel for the snake game so that it can be displayed
 */
public class SnakeBoardPanel extends JPanel implements ActionListener {
    //Width of the playing field (board)
    private final int BOARD_WIDTH = 500;
    //Height of the playing field (board)
    private final int BOARD_HEIGHT = 500;
    //Size of the snake's body
    private final int CIRCLE_SIZE = 10;
    //Maximum size that the snake can have in the game
    private final int MAX_CIRCLE_POSSIBLE = 2500;
    //Randomly placing new fruits for the snake to eat
    private final int RANDOM_TARGET_POSITION = 20;
    // Timer step interval
    private final int STEP_TIMER = 180;

    private final int x_pos[] = new int[MAX_CIRCLE_POSSIBLE];
    private final int y_pos[] = new int[MAX_CIRCLE_POSSIBLE];

    private int snake_current_size;
    private int target_x_pos;
    private int target_y_pos;
    // Direction flags
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean stillInGame = true;
    // Timer for game steps
    private Timer timer;
    // Images for the snake and target
    private Image circle;
    private Image target;
    private Image face;

    /**
     * Constructor that initializes the game panel
     */
    public SnakeBoardPanel() {
        initializeGamePanel();
    }

    /**
     * Initializes the game panel by setting up key listeners,
     * loading images, and starting the game.
     */
    private void initializeGamePanel() {
        addKeyListener(new KeyListener()  {
            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_A) && (!rightDirection)) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                else if ((key == KeyEvent.VK_D) && (!leftDirection)) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                else if ((key == KeyEvent.VK_W) && (!downDirection)) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                else if ((key == KeyEvent.VK_S) && (!upDirection)) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        });
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadGameImageComponents();
        startGame();
    }

    /**
     * Loads the images that are used in the game
     */
    private void loadGameImageComponents() {

        //Body of the snake
        ImageIcon circleIcon = new ImageIcon("./resources/circleIcon.png");
        //Targets (food that the snake has to reach)
        ImageIcon targetIcon = new ImageIcon("./resources/targetIcon.png");
        //Face of the snake (front of the snake)
        ImageIcon faceIcon = new ImageIcon("./resources/faceIcon.png");

        circle = circleIcon.getImage();
        target = targetIcon.getImage();
        face = faceIcon.getImage();
    }

    /**
     * When the game starts, the size of the snake will be 3 circles
     */
    private void startGame() {
        snake_current_size = 3;
        for (int count = 0; count < snake_current_size; count++) {
            x_pos[count] = 100 - count * 10;
            y_pos[count] = 100;
        }

        placeTargetAtANewLocation();

        timer = new Timer(STEP_TIMER, this);
        timer.start();
    }

    /**
     * Overrides the paint component to draw the game.
     *
     * @param g Graphics object used for drawing.
     */
    @Override
    // Overrides the paint component to draw the game.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draws the game elements (snake and target).
        doDrawing(g);
    }

    /**
     * Draws the game elements (snake and target).
     *
     * @param g Graphics object used for drawing.
     */
    private void doDrawing(Graphics g) {
        if (stillInGame) {
            g.drawImage(target, target_x_pos, target_y_pos, this);
            for (int z = 0; z < snake_current_size; z++) {
                if (z == 0) {
                    g.drawImage(face, x_pos[z], y_pos[z], this);
                } else {
                    g.drawImage(circle, x_pos[z], y_pos[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameEnd(g);
        }
    }

    /**
     *When person loses the game, there will be a screen signaling the end of the game.When person loses the game, there will be a screen signaling the end of the game.
     */
    private void gameEnd(Graphics g) {
        String msg = "Game End";
        Font small = new Font("Arial", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
        if(GameEndController.checkGamesWon()){
            EndingApp.main(new String[]{});
        } else {
            HubApp.main(new String[]{});
        }
    }

    /**
     * Checks if the snake has reached the target.
     * If so, increases the snake size and places a new target.
     */
    private void checkTarget() {

        if ((x_pos[0] == target_x_pos) && (y_pos[0] == target_y_pos)) {
            snake_current_size++;
            placeTargetAtANewLocation();
        }
    }
    /**
     * Moves the snake based on the current direction.
     */
    private void moveSnake() {
        for (int count = snake_current_size; count > 0; count--) {
            x_pos[count] = x_pos[(count - 1)];
            y_pos[count] = y_pos[(count - 1)];
        }
        if (leftDirection) {
            x_pos[0] -= CIRCLE_SIZE;
        }
        else if (rightDirection) {
            x_pos[0] += CIRCLE_SIZE;
        }
        else if (upDirection) {
            y_pos[0] -= CIRCLE_SIZE;
        }
        else if (downDirection) {
            y_pos[0] += CIRCLE_SIZE;
        }
    }
    /**
     * Checks if the game is over by verifying if the snake
     * has collided with itself or the borders.
     */
    private void checkIfGameOver() {
        for (int count = snake_current_size; count > 0; count--) {
            if ((count > 4) && (x_pos[0] == x_pos[count]) && (y_pos[0] == y_pos[count])) {
                stillInGame = false;
            }
        }
        if (y_pos[0] >= BOARD_HEIGHT) {
            stillInGame = false;
        }
        else if (y_pos[0] < 0) {
            stillInGame = false;
        }
        else if (x_pos[0] >= BOARD_WIDTH) {
            stillInGame = false;
        }
        else if (x_pos[0] < 0) {
            stillInGame = false;
        }


        if (!stillInGame) {
            timer.stop();
        }
    }

    /**
     * Places the target (food) at a new random location within the board.
     */
    private void placeTargetAtANewLocation() {
        int r = (int) (Math.random() * RANDOM_TARGET_POSITION);
        target_x_pos = ((r * CIRCLE_SIZE));

        r = (int) (Math.random() * RANDOM_TARGET_POSITION);
        target_y_pos = ((r * CIRCLE_SIZE));
    }
    /**
     * Handles action events from the timer.
     * Checks target, game over conditions, and moves the snake.
     *
     * @param e ActionEvent triggered by the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (stillInGame) {
            checkTarget();
            checkIfGameOver();
            moveSnake();
        }
        repaint();
    }
    /**
     * Inner class to handle key events for controlling the snake's direction.
     */
    private class KeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_A) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_D) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_W) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_S) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
