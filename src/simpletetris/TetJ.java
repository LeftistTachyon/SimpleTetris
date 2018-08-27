package simpletetris;

import java.awt.Color;

/**
 * A class that represents the J tetromino
 * @author Danny Tang
 */
public class TetJ extends Tetromino{
    /**
     * Shorthand
     */
    private static final Color b = Color.BLUE;
   
    @Override
    public Color[][] getUp() {
        return new Color[][]{{b,    null, null}, 
                             {b,    b,    b   }, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, b,    null}, 
                             {null, b,    null}, 
                             {b,    b,    null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {b,    b,    b   }, 
                             {null, null, b   }};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, b,    b   }, 
                             {null, b,    null}, 
                             {null, b,    null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}
