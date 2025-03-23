class BasketballGame extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private SportsGame mainGame;
    private Timer gameTimer;
    private BasketballBall basketball;
    private BasketballHoop hoop;
    private boolean shooting = false;
    private boolean scored = false;
    private int score = 0;
    private int attempts = 0;
    private Point mousePos = new Point(0, 0);
    private JButton customizeBtn, backBtn;
    private JSlider hoopHeightSlider;
    private JButton colorRedBtn, colorOrangeBtn, colorPurpleBtn;
    private Color basketballColor = new Color(255, 140, 0);
    private int hoopHeight = 300;
    
    public BasketballGame(SportsGame mainGame) {
        this.mainGame = mainGame;
        setLayout(null);
        
        // Initialize game objects
        basketball = new BasketballBall(100, 450, 40, basketballColor);
        hoop = new BasketballHoop(600, hoopHeight, 120, 80);
        
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
            resetGame();
            mainGame.switchPanel(mainGame.getComponent(0));
        });
        add(backBtn);
        
        customizeBtn = new JButton("Customize");
        customizeBtn.setBounds(20, 60, 150, 30);
        customizeBtn.addActionListener(e -> showCustomizeDialog());
        add(customizeBtn);
    }
    
    private void showCustomizeDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Customize Basketball Game");
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(3, 1));
        
        // Hoop height control
        JPanel hoopPanel = new JPanel();
        hoopPanel.setBorder(BorderFactory.createTitledBorder("Hoop Height"));
        hoopHeightSlider = new JSlider(JSlider.HORIZONTAL, 200, 400, hoopHeight);
        hoopHeightSlider.setMajorTickSpacing(50);
        hoopHeightSlider.setPaintTicks(true);
        hoopHeightSlider.setPaintLabels(true);
        hoopPanel.add(hoopHeightSlider);
        
        // Ball color controls
        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createTitledBorder("Basketball Color"));
        colorRedBtn = new JButton("Red");
        colorRedBtn.setBackground(Color.RED);
        colorRedBtn.addActionListener(e -> {
            basketballColor = Color.RED;
            basketball.color = basketballColor;
            repaint();
        });
        
        colorOrangeBtn = new JButton("Orange");
        colorOrangeBtn.setBackground(new Color(255, 140, 0));
        colorOrangeBtn.addActionListener(e -> {
            basketballColor = new Color(255, 140, 0);
            basketball.color = basketballColor;
            repaint();
        });
        
        colorPurpleBtn = new JButton("Purple");
        colorPurpleBtn.setBackground(new Color(128, 0, 128));
        colorPurpleBtn.addActionListener(e -> {
            basketballColor = new Color(128, 0, 128);
            basketball.color = basketballColor;
            repaint();
        });
        
        colorPanel.add(colorRedBtn);
        colorPanel.add(colorOrangeBtn);
        colorPanel.add(colorPurpleBtn);
        
        // Apply button
        JButton applyBtn = new JButton("Apply Changes");
        applyBtn.addActionListener(e -> {
            hoopHeight = hoopHeightSlider.getValue();
            hoop.y = hoopHeight;
            dialog.dispose();
            repaint();
        });
        
        dialog.add(hoopPanel);
        dialog.add(colorPanel);
        dialog.add(applyBtn);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
    
    public void resetGame() {
        basketball = new BasketballBall(100, 450, 40, basketballColor);
        shooting = false;
        scored = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (shooting) {
            basketball.update();
            
            // Check for scoring
            if (hoop.checkScore(basketball) && !scored) {
                scored = true;
                score++;
            }
            
            // Check if ball is out of bounds
            if (basketball.y > 600 || basketball.x > 800 || basketball.x < -50) {
                gameTimer.stop();
                resetGame();
            }
            
            repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw background
        g2d.setColor(new Color(40, 40, 60));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw court
        g2d.setColor(new Color(237, 201, 175));
        g2d.fillRect(0, 500, getWidth(), 100);
        
        // Draw objects
        hoop.draw(g2d);
        basketball.draw(g2d);
        
        // Draw shooting trajectory if not shooting
        if (!shooting && basketball.vx == 0 && basketball.vy == 0) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2d.drawLine((int)basketball.x, (int)basketball.y, mousePos.x, mousePos.y);
        }
        
        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + score + "/" + attempts, 650, 50);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (!shooting) {
            double dx = e.getX() - basketball.x;
            double dy = e.getY() - basketball.y;
            double power = Math.min(Math.sqrt(dx*dx + dy*dy) / 10, 15);
            
            basketball.vx = dx / 10;
            basketball.vy = dy / 10;
            
            shooting = true;
            attempts++;
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
