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
        return new Color[][]{{null, G,    null}, 
                             {G,    G,    null}, 
                             {G,    null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{G,    G,    null}, 
                             {null, G,    G}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, G}, 
                             {null, G,    G}, 
                             {null, G,    null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, null}, 
                             {G,    G,    null}, 
                             {null, G,    G}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }

    @Override
    public Tetromino copy() {
        TetS s = new TetS();
        s.rotation = rotation;
        return s;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetS;
    }
}