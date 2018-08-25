package simpletetris;

import java.awt.Color;
import static java.awt.Color.*;

/**
 * A class that represents the O tetromino
 * @author Grace Liu, Jed Wang
 */
public class TetO extends Tetromino {
    /**
     * The piece; doesn't change
     */
    private static final Color[][] piece = 
            new Color[][]{{yellow,yellow},{yellow,yellow}};
    
    @Override
    public Color[][] getUp() {
        return piece;
    }

    @Override
    public Color[][] getLeft() {
        return piece;
    }

    @Override
    public Color[][] getDown() {
        return piece;
    }

    @Override
    public Color[][] getRight() {
        return piece;
    }

    @Override
    public int getRotationBoxWidth() {
        return 2;
    }
}
