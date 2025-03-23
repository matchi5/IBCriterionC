package IBCriterionC;

class BasketballHoop {
    int x, y;
    int width, height;

    public BasketballHoop(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean checkScore(BasketballBall ball) {
        // Check if ball passes through the hoop
        if (ball.x > x && ball.x < x + width/2 &&
                ball.y > y && ball.y < y + 10 && ball.vy > 0) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        // Draw backboard
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + width/2, y - 70, 20, 100);

        // Draw rim
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(3));
        g.drawLine(x, y, x + width/2, y);

        // Draw net
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < 10; i++) {
            g.drawLine(x + (i * width/20), y, x + width/4, y + height);
            g.drawLine(x + width/2 - (i * width/20), y, x + width/4, y + height);
        }
        for (int i = 1; i < 5; i++) {
            g.drawArc(x, y + (i * height/5), width/2, 20, 0, 180);
        }
    }
}
