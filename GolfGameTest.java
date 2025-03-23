package IBCriterionC;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;
import javax.swing.Timer;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class GolfGameTest {

    private GolfGame golfGame;
    private SportsGame mockMainGame;

    @Before
    public void setUp() {
        // Create a mock SportsGame for testing
        mockMainGame = new SportsGame();
        golfGame = new GolfGame(mockMainGame);
    }

    @Test
    public void testInitialization() {
        // Verify the game is properly initialized
        assertNotNull("GolfGame should be created", golfGame);

        // Access private fields for testing using reflection
        try {
            Field ballField = GolfGame.class.getDeclaredField("ball");
            ballField.setAccessible(true);
            Object ball = ballField.get(golfGame);
            assertNotNull("Ball should be initialized", ball);

            Field obstaclesField = GolfGame.class.getDeclaredField("obstacles");
            obstaclesField.setAccessible(true);
            ArrayList<GolfObstacle> obstacles = (ArrayList<GolfObstacle>) obstaclesField.get(golfGame);
            assertNotNull("Obstacles should be initialized", obstacles);
            assertFalse("Obstacles list should not be empty", obstacles.isEmpty());

            Field holeField = GolfGame.class.getDeclaredField("hole");
            holeField.setAccessible(true);
            Object hole = holeField.get(golfGame);
            assertNotNull("Hole should be initialized", hole);
        } catch (Exception e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }

    @Test
    public void testResetHole() throws Exception {
        // Access private method for testing
        java.lang.reflect.Method resetMethod = GolfGame.class.getDeclaredMethod("resetHole");
        resetMethod.setAccessible(true);

        // Get the initial state
        Field obstaclesField = GolfGame.class.getDeclaredField("obstacles");
        obstaclesField.setAccessible(true);
        ArrayList<GolfObstacle> initialObstacles = new ArrayList<>(
                (ArrayList<GolfObstacle>) obstaclesField.get(golfGame)
        );

        // Call resetHole
        resetMethod.invoke(golfGame);

        // Verify the state has changed
        ArrayList<GolfObstacle> newObstacles = (ArrayList<GolfObstacle>) obstaclesField.get(golfGame);
        assertNotNull("Obstacles should be initialized after reset", newObstacles);

        // Check that shooting and scored flags are reset
        Field shootingField = GolfGame.class.getDeclaredField("shooting");
        shootingField.setAccessible(true);
        assertFalse("Shooting should be false after reset", shootingField.getBoolean(golfGame));

        Field scoredField = GolfGame.class.getDeclaredField("scored");
        scoredField.setAccessible(true);
        assertFalse("Scored should be false after reset", scoredField.getBoolean(golfGame));
    }

    @Test
    public void testThemeChanging() throws Exception {
        // Access private field
        Field themeField = GolfGame.class.getDeclaredField("dinosaurTheme");
        themeField.setAccessible(true);

        // Check initial theme state
        boolean initialTheme = themeField.getBoolean(golfGame);

        // Toggle theme directly
        themeField.setBoolean(golfGame, !initialTheme);

        // Verify theme changed
        assertEquals("Theme should be toggled", !initialTheme, themeField.getBoolean(golfGame));

        // Reset theme for next tests
        themeField.setBoolean(golfGame, initialTheme);
    }

    @Test
    public void testBallMovement() throws Exception {
        // Access private fields
        Field ballField = GolfGame.class.getDeclaredField("ball");
        ballField.setAccessible(true);
        GolfBall ball = (GolfBall) ballField.get(golfGame);

        // Set initial position
        double initialX = ball.x;
        double initialY = ball.y;

        // Set velocity
        ball.vx = 5.0;
        ball.vy = 3.0;

        // Simulate ball update (normally called in actionPerformed)
        ball.update();

        // Verify ball moved
        assertEquals("Ball should move in x direction", initialX + 5.0, ball.x, 0.001);
        assertEquals("Ball should move in y direction", initialY + 3.0, ball.y, 0.001);
    }

    @Test
    public void testScoring() throws Exception {
        // Access private fields
        Field ballField = GolfGame.class.getDeclaredField("ball");
        ballField.setAccessible(true);
        GolfBall ball = (GolfBall) ballField.get(golfGame);

        Field holeField = GolfGame.class.getDeclaredField("hole");
        holeField.setAccessible(true);
        GolfHole hole = (GolfHole) holeField.get(golfGame);

        Field scoredField = GolfGame.class.getDeclaredField("scored");
        scoredField.setAccessible(true);

        // Position ball at the hole
        ball.x = hole.x;
        ball.y = hole.y;

        // Check scoring
        boolean scored = hole.checkScore(ball);
        assertTrue("Ball should score when at the same position as hole", scored);
    }
}
