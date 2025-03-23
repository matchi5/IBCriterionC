package IBCriterionC;

class GolfBall {
    double x, y;
    double vx = 0, vy = 0;
    int radius;

    public GolfBall(double x, double y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public void applyFriction() {
        vx *= 0.98;
        vy *= 0.98;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);

        // Draw golf ball dimples
        g.setColor(new Color(220, 220, 220));
        for (int i = 0; i < 8; i++) {
            int dx = (int)(radius * 0.5 * Math.cos(i * Math.PI/4));
            int dy = (int)(radius * 0.5 * Math.sin(i * Math.PI/4));
            g.fillOval((int)(x + dx - 2), (int)(y + dy - 2), 4, 4);
        }
    }
}
