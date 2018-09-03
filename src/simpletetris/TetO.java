package simpletetris;

import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Point;

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

    @Override
    public Point getWallKick(TetrisMatrix tm, int rotateTo) {
        // Sanity check
        if(!sameTetromino(tm.getFallingPiece()))
            throw new IllegalStateException("Unable to determine wallkick");
        
        return new Point(0, 0); // O tetrominos don't kick
    }

    @Override
    public Tetromino copy() {
        return new TetO();
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetO;
    }

    @Override
    public Color getColor() {
        return new Color(243, 205, 35);
    }
}
