package simpletetris;

import java.awt.Color;

/**
 * A class that represents the J tetromino
 * @author Danny Tang, Jed Wang
 */
public class TetJ extends Tetromino{
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
}
