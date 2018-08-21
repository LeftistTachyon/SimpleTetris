package simpletetris;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * The JPanel where all of the images are drawn
 * @author Jed Wang
 */
public class TetrisPanel extends JPanel implements Runnable {
    /**
     * The matrix being shown.
     */
    private TetrisMatrix matrix;

    /**
     * Creates a new TetrisPanel.
     */
    public TetrisPanel() {
        matrix = new TetrisMatrix();
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
    }

    @Override
    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.err.println("Interrupted.");
            }
        }
    }
}