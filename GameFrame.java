package hillcrest.project.snake;//Panel so that the game can be displayed
import javax.swing.*;

/**
 * This class extends JFrame and initializes the game board panel.
 */
public class GameFrame extends JFrame {

    /**
     * Constructor that calls initBoardPanel().
     */
    public GameFrame() {
        initBoardPanel();
    }


    /**
     * Initializes the board panel
     */
    private void initBoardPanel() {
        add(new hillcrest.project.snake.SnakeBoardPanel());
        //Width by height size of the panel
        setSize(500,500);
        //Title of the panel (it wil show on the top left)
        setTitle("Snake Board Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * Runs the game
     * @param args String[] args
     */
    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        //Make the frame visible
        gameFrame.setVisible(true);
    }
}

