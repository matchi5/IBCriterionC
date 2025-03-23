package IBCriterionC;

class GolfHole {
    int x, y;
    int radius;

    public GolfHole(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean checkScore(GolfBall ball) {
        // Check if ball is in the hole
        double distance = Math.sqrt(Math.pow(ball.x - x, 2) + Math.pow(ball.y - y, 2));
        return distance < radius - ball.radius;
    }

    public void draw(Graphics2D g) {
        // Draw hole
        g.setColor(Color.BLACK);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // Draw flag
        g.setColor(Color.RED);
        g.fillRect(x + radius - 5, y - 50, 20, 15);
        g.setColor(Color.GRAY);
        g.fillRect(x + radius, y - 50, 5, 50);
    }
}
