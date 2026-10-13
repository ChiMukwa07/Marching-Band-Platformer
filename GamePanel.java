import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    // Screen Settings
    final int originalTileSize = 18; // 18x18 tile
    final int scale = 3;
    final int tileSize = originalTileSize * scale; // 54x54 tile
    final int maxScreenCol = 22;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 864px
    final int screenHeight = tileSize * maxScreenRow; // 576px

    // System
    Thread gameThread;

    // Player State Variables
    int playerX = 100;
    int playerY = 300;
    int playerWidth = tileSize;
    int playerHeight = tileSize;
    
    // Physics Engine Basics
    double yVelocity = 0;
    double xVelocity = 0;
    // double gravity = 0.5;
    boolean isGrounded = false;
    boolean leftPressed, rightPressed, downPressed, upPressed;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GREEN);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60; // 0.0166 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // 1: Update game logic (positions/physics)
                repaint(); // 2: Draw the screen
                delta--;
            }
        }
    }

    // Physics & Logic Updates
    public void update() {
        // Horizontal Movement
        if (leftPressed) playerX -= 3;
        if (rightPressed) playerX += 3;

        //Vertical Movement
        if (upPressed) playerY -= 3;
        if (downPressed) playerY += 3;

        // Gravity Application
        // yVelocity += gravity;
        playerY += yVelocity;
        playerX += xVelocity;

        // Temporary Ground Collision (Bottom of screen minus player height)
        int groundLevel = screenHeight - tileSize - 20;
        if (playerY >= groundLevel) {
            playerY = groundLevel;
            yVelocity = 0;
            isGrounded = true;
        } else {
            isGrounded = false;
        }

        int wallsLeft = 0;
        if (playerX <= wallsLeft) {
            playerX = wallsLeft;
            xVelocity = 0;
        }

        int wallsRight = screenWidth - tileSize;
        if (playerX >= wallsRight) {
            playerX = wallsRight;
            xVelocity = 0;
        }

        int top = 0;
        if (playerY <= top) {
            playerY = top;
            yVelocity = 0;
        }

    }

    // Rendering Graphics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw Environment Ground
        g2.setColor(Color.WHITE);
        g2.fillRect(0, screenHeight - tileSize + 20, screenWidth, tileSize -35);

        // --- DRAW VERTICAL FIELD MARKERS ---
        g2.setColor(Color.WHITE); // Choose a color for the markers
        
        int markerSpacing = screenWidth/10; // Distance between each vertical line in pixels
        int markerHeight = screenHeight;   // Height of the markers
        
        // Loop from 0 up to the screen width, drawing a line at each interval
        for (int x = 0; x < screenWidth; x += markerSpacing) {
            // Draw lines sticking up from the ground line
            int groundY = screenHeight - tileSize + 20;
            g2.fillRect(x, groundY - screenHeight, 10, markerHeight);
        }

        // Draw Player Sprite (Square Placeholder)
        g2.setColor(Color.MAGENTA);
        g2.fillRect(playerX, playerY, playerWidth, playerHeight);

        g2.dispose();
    }

    // Keyboard Input Listeners
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
