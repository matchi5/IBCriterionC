package IBCriterionC

class BasketballBall {
    double x, y;
    double vx = 0, vy = 0;
    int radius;
    Color color;
    double gravity = 0.5;

    public BasketballBall(double x, double y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public void update() {
        x += vx;
        y += vy;
        vy += gravity;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);

        // Draw basketball lines
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        g.drawArc((int)(x - radius), (int)(y - radius), radius * 2, radius * 2, 0, 180);
        g.drawArc((int)(x - radius), (int)(y - radius), radius * 2, radius * 2, 90, 180);
    }
}
