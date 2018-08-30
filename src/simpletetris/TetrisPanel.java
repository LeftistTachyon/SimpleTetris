package simpletetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * The JPanel where all of the images are drawn
 * @author Jed Wang
 */
public class TetrisPanel extends JPanel implements Runnable {
    /**
     * The matrix being shown.
     */
    public final TetrisMatrix matrix;
    
    /**
     * The KeyAdapter which is listening in to key presses
     */
    private TetrisKeyAdapter tka;

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
        drawBackground(g2D, Color.WHITE);
        
        matrix.draw(g2D);
    }
    
    /**
     * Fills the background with a solid color
     * @param g2D the Graphics2D to draw with
     * @param c the color to fill with
     */
    public void drawBackground(Graphics2D g2D, Color c) {
        g2D.setColor(c);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void drawBackground(Graphics2D g2D, BufferedImage bi) {
        g2D.drawImage(bi, 0, 0, getWidth(), getHeight(), null);
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
