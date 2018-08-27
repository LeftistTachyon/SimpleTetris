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
    private static final Color G = Color.green;
    
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, G,    G}, 
                             {G,    G,    null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{G,    null, null}, 
                             {G,    G,    null}, 
                             {null, G,    null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {null, G,    G}, 
                             {G,    G,    null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, G,    null}, 
                             {null, G,    G}, 
                             {null, null, G}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}