package simpletetris;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class that represents the J tetromino
 * @author Danny Tang, Jed Wang
 */
public class TetJ extends Tetromino {
    /**
     * A mini TetJ
     */
    private static BufferedImage mini;
    
    static {
        try {
            mini = ImageIO.read(new File("image/miniJ.png"));
        } catch (IOException ex) {
            System.err.println("miniJ.png cannot be found");
        }
    }
    
    /**
     * Shorthand
     */
    private static final Color B = Color.BLUE;
   
    @Override
    public Color[][] getUp() {
        return new Color[][]{{B,    B,    null}, 
                             {null, B,    null}, 
                             {null, B,    null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, null, B}, 
                             {B,    B,    B}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, B,    null}, 
                             {null, B,    null}, 
                             {null, B,    B}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, null}, 
                             {B,    B,    B}, 
                             {B,    null, null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }

    @Override
    public Tetromino copy() {
        TetJ j = new TetJ();
        j.rotation = rotation;
        return j;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetJ;
    }

    @Override
    public Color getColor() {
        return new Color(17, 85, 180);
    }

    @Override
    public BufferedImage getMiniImage() {
        return mini;
    }
}
