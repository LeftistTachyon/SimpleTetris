package simpletetris;

import java.awt.Color;

/**
 * A class that represents the S tetromino
 * @author Jed Wang
 */
public class TetS extends Tetromino {
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, null, null}, 
                             {null, null, null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, null, null}, 
                             {null, null, null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {null, null, null}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, null}, 
                             {null, null, null}, 
                             {null, null, null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}