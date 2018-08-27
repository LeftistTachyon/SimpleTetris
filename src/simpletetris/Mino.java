package simpletetris;

import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class that controls the drawing of each, individual block / mino.
 * @author Jed Wang
 */
public class Mino {
    /**
     * A blue mino.
     */
    public static final BufferedImage BLUE;
    
    /**
     * A cyan mino.
     */
    public static final BufferedImage CYAN;
    
    /**
     * A green mino.
     */
    public static final BufferedImage GREEN;
    
    /**
     * A grey mino.
     * This is a garbage block.
     */
    public static final BufferedImage GREY;
    
    /**
     * An orange mino.
     */
    public static final BufferedImage ORANGE;
    
    /**
     * A purple mino.
     * Use {@code Color.magenta} to denote this mino.
     */
    public static final BufferedImage PURPLE;
    
    /**
     * A red mino.
     */
    public static final BufferedImage RED;
    
    /**
     * A yellow mino.
     */
    public static final BufferedImage YELLOW;
    
    /**
     * Loading all image files.
     */
    static {
        BufferedImage[] temp = new BufferedImage[8];
        
        try {
            temp[0] = ImageIO.read(new File("images/blue.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find BLUE mino");
            System.exit(1);
        }
        BLUE = temp[0];
        
        try {
            temp[1] = ImageIO.read(new File("images/cyan.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find CYAN mino");
            System.exit(1);
        }
        CYAN = temp[1];
        
        try {
            temp[2] = ImageIO.read(new File("images/green.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find GREEN mino");
            System.exit(1);
        }
        GREEN = temp[2];
        
        try {
            temp[3] = ImageIO.read(new File("images/grey.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find GREY mino");
            System.exit(1);
        }
        GREY = temp[3];
        
        try {
            temp[4] = ImageIO.read(new File("images/orange.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find ORANGE mino");
            System.exit(1);
        }
        ORANGE = temp[4];
        
        try {
            temp[5] = ImageIO.read(new File("images/purple.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find PURPLE mino");
            System.exit(1);
        }
        PURPLE = temp[5];
        
        try {
            temp[6] = ImageIO.read(new File("images/red.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find RED mino");
            System.exit(1);
        }
        RED = temp[6];
        
        try {
            temp[7] = ImageIO.read(new File("images/yellow.png"));
        } catch (IOException ex) {
            System.err.println("Couldn\'t find YELLOW mino");
            System.exit(1);
        }
        YELLOW = temp[7];
    }
    
    /**
     * The width of a mino
     */
    public static final int MINO_WIDTH = 36;
    
    /**
     * Can't touch this
     */
    private Mino() {}
    
    /**
     * Draws a mino of a given color.
     * If the color is null or not a stored color, nothing is drawn.
     * @param x the x-coordinate of the top left corner
     * @param y the y-coordinate of the top left corner
     * @param c the color of the mino to draw
     * @param g2D the Graphics2D to draw with
     */
    public static void drawMino(int x, int y, Color c, Graphics2D g2D) {
        if(c == null) 
            return;
        int rgb = c.getRGB();
        if(rgb == blue.getRGB()) {
            g2D.drawImage(BLUE, null, x, y);
        } else if(rgb == cyan.getRGB()) {
            g2D.drawImage(CYAN, null, x, y);
        } else if(rgb == green.getRGB()) {
            g2D.drawImage(GREEN, null, x, y);
        } else if(rgb == gray.getRGB()) {
            g2D.drawImage(GREY, null, x, y);
        } else if(rgb == orange.getRGB()) {
            g2D.drawImage(ORANGE, null, x, y);
        } else if(rgb == magenta.getRGB()) {
            g2D.drawImage(PURPLE, null, x, y);
        } else if(rgb == red.getRGB()) {
            g2D.drawImage(RED, null, x, y);
        } else if(rgb == yellow.getRGB()) {
            g2D.drawImage(YELLOW, null, x, y);
        }
    }
}