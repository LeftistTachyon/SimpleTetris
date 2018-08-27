package simpletetris;

import java.awt.Color;

/**
 * A class that represents the S tetromino
 * @author Jed Wang
 */
public class TetS extends Tetromino {
    /**
     * Shorthand
     */
    private static final Color GREE = Color.green;
    
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, GREE, GREE}, 
                             {GREE, GREE, null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{GREE, null, null}, 
                             {GREE, GREE, null}, 
                             {null, GREE, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {null, GREE, GREE}, 
                             {GREE, GREE, null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, GREE, null}, 
                             {null, GREE, GREE}, 
                             {null, null, GREE}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}