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
    private static Color o = Color.ORANGE;

    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, null, o}, 
                             {o,    o,    o}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{o,    o,    null}, 
                             {null, o,    null}, 
                             {null, o,    null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {o,    o,    o}, 
                             {o,    null, null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, o,    null}, 
                             {null, o,    null}, 
                             {null, o,    o}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}