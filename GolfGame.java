package IBCriterionC;

class GolfGame extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private SportsGame mainGame;
    private Timer gameTimer;
    private GolfBall ball;
    private java.util.ArrayList<GolfObstacle> obstacles;
    private GolfHole hole;
    private boolean shooting = false;
    private boolean scored = false;
    private int strokes = 0;
    private int currentHole = 1;
    private Point mousePos = new Point(0, 0);
    private JButton backBtn, customizeBtn;
    private boolean dinosaurTheme = false;
    private Image clubImage;

    public GolfGame(SportsGame mainGame) {
        this.mainGame = mainGame;
        setLayout(null);

        // Load club image
        try {
            clubImage = new ImageIcon("resources/club.png").getImage();
            if (clubImage.getWidth(null) <= 0) {
                clubImage = null;
            }
        } catch (Exception e) {
            clubImage = null;
        }

        // Initialize game objects
        resetHole();

        // Set up interaction
        addMouseListener(this);
        addMouseMotionListener(this);

        // Game timer
        gameTimer = new Timer(20, this);

        // Controls
        backBtn = new JButton("Back to Menu");
        backBtn.setBounds(20, 20, 150, 30);
        backBtn.addActionListener(e -> {
            gameTimer.stop();
            mainGame.switchPanel(mainGame.getComponent(0));
        });
        add(backBtn);

        customizeBtn = new JButton("Customize Course");
        customizeBtn.setBounds(20, 60, 150, 30);
        customizeBtn.addActionListener(e -> showCustomizeDialog());
        add(customizeBtn);
    }

    private void showCustomizeDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Customize Golf Course");
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(3, 1));

        // Theme options
        JPanel themePanel = new JPanel();
        themePanel.setBorder(BorderFactory.createTitledBorder("Course Theme"));

        JRadioButton standardTheme = new JRadioButton("Standard Course");
        standardTheme.setSelected(!dinosaurTheme);

        JRadioButton dinoTheme = new JRadioButton("Dinosaur Theme");
        dinoTheme.setSelected(dinosaurTheme);

        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(standardTheme);
        themeGroup.add(dinoTheme);

        themePanel.add(standardTheme);
        themePanel.add(dinoTheme);

        // Apply button
        JButton applyBtn = new JButton("Apply Changes");
        applyBtn.addActionListener(e -> {
            dinosaurTheme = dinoTheme.isSelected();
            resetHole();
            dialog.dispose();
            repaint();
        });

        dialog.add(themePanel);
        dialog.add(new JLabel("Changes will apply to the next hole"));
        dialog.add(applyBtn);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void resetHole() {
        // Create a new course layout
        ball = new GolfBall(100, 300, 15);
        obstacles = new java.util.ArrayList<>();

        // Create different course layouts based on the current hole
        java.util.Random rand = new java.util.Random();

        // Add sand traps and water hazards
        for (int i = 0; i < 3 + currentHole; i++) {
            int type = rand.nextInt(2);
            int x = 150 + rand.nextInt(450);
            int y = 100 + rand.nextInt(350);
            int width = 50 + rand.nextInt(100);
            int height = 30 + rand.nextInt(50);

            obstacles.add(new GolfObstacle(x, y, width, height, type));
        }

        // Add hole at a random position (but farther as levels progress)
        int holeX = 600 + rand.nextInt(100);
        int holeY = 100 + rand.nextInt(400);
        hole = new GolfHole(holeX, holeY, 20);

        shooting = false;
        scored = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (shooting && !scored) {
            ball.update();

            // Check collisions with obstacles
            for (GolfObstacle obstacle : obstacles) {
                obstacle.checkCollision(ball);
            }

            // Apply friction
            ball.applyFriction();

            // Check if ball stopped moving
            if (Math.abs(ball.vx) < 0.1 && Math.abs(ball.vy) < 0.1) {
                ball.vx = 0;
                ball.vy = 0;
                shooting = false;
            }

            // Check for scoring
            if (hole.checkScore(ball) && !scored) {
                scored = true;

                // Show success message and move to next hole
                Timer nextHoleTimer = new Timer(2000, evt -> {
                    currentHole++;
                    strokes = 0;
                    resetHole();
                    ((Timer)evt.getSource()).stop();
                });
                nextHoleTimer.setRepeats(false);
                nextHoleTimer.start();
            }

            // Check if ball is out of bounds
            if (ball.x < 0 || ball.x > 800 || ball.y < 0 || ball.y > 600) {
                // Reset ball position with penalty
                ball = new GolfBall(100, 300, 15);
                shooting = false;
                strokes++;
            }

            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw background based on theme
        if (dinosaurTheme) {
            g2d.setColor(new Color(100, 150, 100)); // Jungle green
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Draw dinosaur silhouettes
            g2d.setColor(new Color(30, 80, 30));
            // Simple dino shape
            int[] xPoints = {650, 670, 690, 700, 710, 715, 730, 740, 730, 720, 710, 700, 680, 660, 650};
            int[] yPoints = {450, 420, 415, 400, 380, 370, 380, 450, 460, 465, 464, 470, 470, 465, 450};
            g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        } else {
            // Standard golf course
            g2d.setColor(new Color(120, 180, 90)); // Grass green
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw obstacles
        for (GolfObstacle obstacle : obstacles) {
            obstacle.draw(g2d, dinosaurTheme);
        }

        // Draw hole
        hole.draw(g2d);

        // Draw ball
        ball.draw(g2d);

        // Draw shooting trajectory if not shooting
        if (!shooting && ball.vx == 0 && ball.vy == 0 && !scored) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2d.drawLine((int)ball.x, (int)ball.y, mousePos.x, mousePos.y);

            // Draw golf club
            if (clubImage != null) {
                double angle = Math.atan2(mousePos.y - ball.y, mousePos.x - ball.x);
                g2d.rotate(angle, mousePos.x, mousePos.y);
                g2d.drawImage(clubImage, mousePos.x - 50, mousePos.y - 10, 50, 20, null);
                g2d.rotate(-angle, mousePos.x, mousePos.y);
            }
        }

        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Hole: " + currentHole, 650, 30);
        g2d.drawString("Strokes: " + strokes, 650, 60);

        // Draw success message
        if (scored) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(250, 250, 300, 100);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("Hole Complete!", 300, 300);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString("Next hole loading...", 310, 330);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!shooting && !scored) {
            double dx = e.getX() - ball.x;
            double dy = e.getY() - ball.y;
            double distance = Math.sqrt(dx*dx + dy*dy);
            double power = Math.min(distance / 20, 10);

            ball.vx = -dx / 20;
            ball.vy = -dy / 20;

            shooting = true;
            strokes++;
            gameTimer.start();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.x = e.getX();
        mousePos.y = e.getY();
        repaint();
    }

    // Unused mouse event methods
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
}
