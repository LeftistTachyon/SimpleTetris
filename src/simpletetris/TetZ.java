package simpletetris;

import java.awt.Color;

/**
 * A class that represents the Z tetromino
 * @author Jed Wang, Danny Tang
 */
public class TetZ extends Tetromino {
    /**
     * Shorthand
     */
    private static final Color r = Color.RED;
    
    @Override
    public Color[][] getUp() {
        return new Color[][]{{r,    null, null}, 
                             {r,    r,    null}, 
                             {null, r,    null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, r,    r}, 
                             {r,    r,    null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, r,    null}, 
                             {null, r,    r}, 
                             {null, null, r}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, r,    r}, 
                             {r,    r,    null}, 
                             {null, null, null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }

    @Override
    public Tetromino copy() {
        TetZ z = new TetZ();
        z.rotation = rotation;
        return z;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetZ;
    }
}