import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Marching Band Simulator");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Sizes the window to fit the gamePanel dimensions

        window.setLocationRelativeTo(null); // Centers window
        window.setVisible(true);

        gamePanel.startGameThread(); // Starts the clock
    }
}
