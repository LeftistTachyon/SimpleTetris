package simpletetris;

import java.awt.Color;

/**
 * A class that represents the T tetromino
 * @author Jed Wang, Danny Tang
 */
public class TetT extends Tetromino {
    /**
     * Shorthand
     */
    private static final Color P = Color.MAGENTA;
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, P,    null}, 
                             {P,    P,    null}, 
                             {null, P,    null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, P,    null}, 
                             {P,    P,    P}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, P,    null}, 
                             {null, P,    P}, 
                             {null, P,    null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, null}, 
                             {P,    P,    P}, 
                             {null, P,    null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }

    @Override
    public Tetromino copy() {
        TetT t = new TetT();
        t.rotation = rotation;
        return t;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetT;
    }

    @Override
    public Color getColor() {
        return new Color(146, 23, 156);
    }
}