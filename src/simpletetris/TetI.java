package simpletetris;

import java.awt.Color;

/**
 * A class that represents the I tetromino
 * @author Grace Liu, Jed Wang
 */
public class TetI extends Tetromino {
    @Override
    public Color[][] getUp() {
        return new Color[][]{{}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 4;
    }
}
