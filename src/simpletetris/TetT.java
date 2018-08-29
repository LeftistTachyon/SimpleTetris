package simpletetris;

import java.awt.Color;

/**
 * A class that represents the T tetromino
 * @author Jed Wang, Danny Tang
 */
public class TetT extends Tetromino {
    Color p = Color.MAGENTA;
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, p, null}, 
                             {p, p, p}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, p, null}, 
                             {p, p, null}, 
                             {null, p, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {p, p, p}, 
                             {null, p, null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, p, null}, 
                             {null, p, p}, 
                             {null, p, null}};
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
}