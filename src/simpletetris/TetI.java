package simpletetris;

import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Point;

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

    @Override
    public Point getWallKick(TetrisMatrix tm, int rotateTo) {
        // Sanity check
        if(!sameTetromino(tm.getFallingPiece()))
            throw new IllegalStateException("Unable to determine wallkick");
        
        if(rotations == 0) return new Point(0, 0);
        
        // Check for wallkicks
        Tetromino copy = copy();
        copy.rotation = rotateTo;
        
        if(!copy.overlaps(tm.miniMatrix())) 
            return new Point(0, 0);
        
        // Manual checks: starting from test 2
        // I tetromino has its own set of kicks
        switch(rotation) {
            case UP:
                switch(rotateTo) {
                    case CLOCKWISE:
                        // (-2, 0) (+1, 0) (-2,-1) (+1,+2)
                        if(!copy.overlaps(tm.miniMatrix(-2, 0)))
                            return new Point(-2, 0);
                        if(!copy.overlaps(tm.miniMatrix(1, 0)))
                            return new Point(1, 0);
                        if(!copy.overlaps(tm.miniMatrix(-2, -1)))
                            return new Point(-2, -1);
                        if(!copy.overlaps(tm.miniMatrix(1, 2)))
                            return new Point(1, 2);
                        break;
                    case COUNTERCLOCKWISE:
                        // (-1, 0) (+2, 0) (-1,+2) (+2,-1)
                        if(!copy.overlaps(tm.miniMatrix(-1, 0)))
                            return new Point(-1, 0);
                        if(!copy.overlaps(tm.miniMatrix(2, 0)))
                            return new Point(2, 0);
                        if(!copy.overlaps(tm.miniMatrix(-2, -1)))
                            return new Point(-2, -1);
                        if(!copy.overlaps(tm.miniMatrix(1, 2)))
                            return new Point(1, 2);
                        break;
                    default:
                        throw new IllegalStateException("Illegal rotation");
                }
                break;
            case RIGHT:
                switch(rotateTo) {
                    case COUNTERCLOCKWISE:
                        // (+2, 0) (-1, 0) (+2,+1) (-1,-2)
                        if(!copy.overlaps(tm.miniMatrix(2, 0)))
                            return new Point(2, 0);
                        if(!copy.overlaps(tm.miniMatrix(-1, 0)))
                            return new Point(-1, 0);
                        if(!copy.overlaps(tm.miniMatrix(2, 1)))
                            return new Point(2, 1);
                        if(!copy.overlaps(tm.miniMatrix(-1, -2)))
                            return new Point(-1, -2);
                        break;
                    case CLOCKWISE:
                        // (-1, 0) (+2, 0) (-1,+2) (+2,-1)
                        if(!copy.overlaps(tm.miniMatrix(-1, 0)))
                            return new Point(-1, 0);
                        if(!copy.overlaps(tm.miniMatrix(2, 0)))
                            return new Point(2, 0);
                        if(!copy.overlaps(tm.miniMatrix(-1, 2)))
                            return new Point(-1, 2);
                        if(!copy.overlaps(tm.miniMatrix(2, -1)))
                            return new Point(2, -1);
                        break;
                    default:
                        throw new IllegalStateException("Illegal rotation");
                }
                break;
            case DOWN:
                switch(rotateTo) {
                    case COUNTERCLOCKWISE:
                        // (+1, 0) (-2, 0) (+1,-2) (-2,+1)
                        if(!copy.overlaps(tm.miniMatrix(1, 0)))
                            return new Point(1, 0);
                        if(!copy.overlaps(tm.miniMatrix(-2, 0)))
                            return new Point(-2, 0);
                        if(!copy.overlaps(tm.miniMatrix(1, -2)))
                            return new Point(1, -2);
                        if(!copy.overlaps(tm.miniMatrix(-2, 1)))
                            return new Point(-2, 1);
                        break;
                    case CLOCKWISE:
                        // (+2, 0) (-1, 0) (+2,+1) (-1,-2)
                        if(!copy.overlaps(tm.miniMatrix(2, 0)))
                            return new Point(2, 0);
                        if(!copy.overlaps(tm.miniMatrix(-1, 0)))
                            return new Point(-1, 0);
                        if(!copy.overlaps(tm.miniMatrix(2, 1)))
                            return new Point(2, 1);
                        if(!copy.overlaps(tm.miniMatrix(-1, -2)))
                            return new Point(-1, -2);
                        break;
                    default:
                        throw new IllegalStateException("Illegal rotation");
                }
                break;
            case LEFT:
                switch(rotateTo) {
                    case CLOCKWISE:
                        // (+1, 0) (-2, 0) (+1,-2) (-2,+1)
                        if(!copy.overlaps(tm.miniMatrix(1, 0)))
                            return new Point(1, 0);
                        if(!copy.overlaps(tm.miniMatrix(-2, 0)))
                            return new Point(-2, 0);
                        if(!copy.overlaps(tm.miniMatrix(1, -2)))
                            return new Point(1, -2);
                        if(!copy.overlaps(tm.miniMatrix(-2, 1)))
                            return new Point(-2, 1);
                        break;
                    case COUNTERCLOCKWISE:
                        // (-2, 0) (+1, 0) (-2,-1) (+1,+2)
                        if(!copy.overlaps(tm.miniMatrix(-2, 0)))
                            return new Point(-2, 0);
                        if(!copy.overlaps(tm.miniMatrix(1, 0)))
                            return new Point(1, 0);
                        if(!copy.overlaps(tm.miniMatrix(-2, -1)))
                            return new Point(-2, -1);
                        if(!copy.overlaps(tm.miniMatrix(1, 2)))
                            return new Point(1, 2);
                        break;
                    default:
                        throw new IllegalStateException("Illegal rotation");
                }
                break;
            default:
                throw new IllegalStateException("rotation should not be the "
                        + "value" + rotation);
        }
        
        throw new IllegalStateException("No transformations worked. "
                + "What do I do now?");
    }

    @Override
    public Tetromino copy() {
        TetI i = new TetI();
        i.rotation = rotation;
        return i;
    }

    @Override
    public boolean sameTetromino(Tetromino t) {
        return t instanceof TetI;
    }
}
