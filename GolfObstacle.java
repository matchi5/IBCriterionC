package IBCriterionC;

class GolfObstacle {
    int x, y, width, height;
    int type; // 0 for sand trap, 1 for water

    public GolfObstacle(int x, int y, int width, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void checkCollision(GolfBall ball) {
        // Check if ball is inside obstacle
        if (ball.x > x && ball.x < x + width && ball.y > y && ball.y < y + height) {
            if (type == 0) { // Sand trap slows the ball
                ball.vx *= 0.9;
                ball.vy *= 0.9;
            } else { // Water hazard stops the ball
                ball.vx = 0;
                ball.vy = 0;
            }
        }
    }

    public void draw(Graphics2D g, boolean dinosaurTheme) {
        if (type == 0) { // Sand trap
            g.setColor(dinosaurTheme ? new Color(210, 180, 140) : new Color(230, 230, 180));
            g.fillOval(x, y, width, height);
        } else { // Water
            g.setColor(dinosaurTheme ? new Color(0, 100, 30) : new Color(70, 130, 180));
            g.fillRoundRect(x, y, width, height, 10, 10);

            // Draw water ripples
            g.setColor(dinosaurTheme ? new Color(0, 120, 40) : new Color(100, 160, 210));
            for (int i = 0; i < 3; i++) {
                g.drawOval(x + 10 + i * 10, y + height/2, 20, 10);
                g.drawOval(x + width/2 + i * 10, y + 10, 20, 10);
            }
        }
    }
}
