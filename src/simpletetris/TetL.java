package simpletetris;

import java.awt.Color;

/**
 * A class that represents the L tetromino
 * @author Jed Wang
 */
public class TetL extends Tetromino {
    /**
     * Shorthand
     */
    private static final Color O = Color.ORANGE;

    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, O,    null}, 
                             {null, O,    null}, 
                             {O,    O,    null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{O,    null, null}, 
                             {O,    O,    O}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, O,    O}, 
                             {null, O,    null}, 
                             {null, O,    null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, null}, 
                             {O,    O,    O}, 
                             {null, null, O}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }

    @Override
    public Tetromino copy() {
        TetL l = new TetL();
        l.rotation = rotation;
        return l;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetL;
    }
}
