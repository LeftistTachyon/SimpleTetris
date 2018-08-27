package simpletetris;

import java.awt.Color;
import static java.awt.Color.*;

/**
 * A class that represents the I tetromino
 * @author Grace Liu, Jed Wang
 */
public class TetI extends Tetromino {
    
    @Override
    public Color[][] getUp() {
        return new Color[][]{{null, null, null, null}, 
                             {cyan, cyan, cyan, cyan}, 
                             {null, null, null, null}, 
                             {null, null, null, null}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{null, cyan, null, null}, 
                             {null, cyan, null, null}, 
                             {null, cyan, null, null}, 
                             {null, cyan, null, null}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null, null, null, null}, 
                             {null, null, null, null}, 
                             {cyan, cyan, cyan, cyan},
                             {null, null, null, null}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, null, cyan, null}, 
                             {null, null, cyan, null}, 
                             {null, null, cyan, null}, 
                             {null, null, cyan, null}};
    }

    @Override
    public int getRotationBoxWidth() {
        return 4;
    }
}
