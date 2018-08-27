package simpletetris;

import java.awt.Color;

/**
 * A class that represents the Z tetromino
 * @author Jed Wang,danny tang
 */
public class TetZ extends Tetromino {
    Color r = Color.RED;
    @Override
    public Color[][] getUp() {
        return new Color[][]{{r, r, null}, 
                             {null, r, r}, 
                             {null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, r, null}, 
                             {r, r, null}, 
                             {r, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null}, 
                             {r, r, null}, 
                             {null, r, r}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, r, null}, 
                             {r, r, null}, 
                             {r, null, null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 3;
    }
}