package simpletetris;

import java.awt.Color;

/**
 * Creates a new Tetromino
 * @author Danny Tang, Jed Wang, Grace Liu
 */
public abstract class Tetromino {
    /**
     * The rotation of the piece
     */
    private int rotation;
    
    /**
     * Keeps track of the number of rotations this tetromino has performed.
     * A max of 15. After that, rotation lock.
     */
    private int rotations = 15;
    
    /**
     * The "up" rotation state
     */
    public static final int UP = 0;
    
    /**
     * The "left" rotation state
     */
    public static final int LEFT = 1;
    
    /**
     * The "down" rotation state
     */
    public static final int DOWN = 2;
    
    /**
     * The "right" rotation state
     */
    public static final int RIGHT = 3;
    
    /**
     * Creates a new Tetromino
     */
    public Tetromino(){
        rotation = 0;
    }
    
    /**
     * Returns the up state of the tetromino
     * @return the up state 
     */
    public abstract Color[][] getUp();
    
    /**
     * Returns the left state of the tetromino
     * @return the left state 
     */
    public abstract Color[][] getLeft();
    
    /**
     * Returns the down state of the tetromino
     * @return the down state 
     */
    public abstract Color[][] getDown();
    
    /**
     * Returns the right state of the tetromino
     * @return the right state 
     */
    public abstract Color[][] getRight();
    
    /**
     * Determines the width of the rotation box
     * @return the width of the rotation box
     */
    public abstract int getRotationBoxWidth();
    
    /**
     * Determines whether this tetromino overlaps any of the colors 
     * of the matrix.
     * @param area the area given: always rotationBoxWidth by rotationBoxWidth
     * @return whether this tetromino overlaps any of the colors of the matrix
     */
    public boolean overlaps(Color[][] area) {
        for(int i = 0; i < area.length; i++) {
            
        }
    }
    
    /**
     * Returns the minos to draw
     * @return the minos to draw
     */
    public Color[][] getDrawBox() {
        switch(rotation) {
            case UP:
                return getUp();
            case RIGHT:
                return getRight();
            case LEFT:
                return getLeft();
            case DOWN:
                return getDown();
        }
        throw new IllegalStateException("rotation has an illegal value");
    }
    
    /**
     * Rotates this tetromino clockwise.
     */
    public void rotateRight() {
        if(rotations == 0) 
            return;
        
        rotation--;
        if(rotation == -1) 
            rotation = 3;
        
        rotations--;
    }
    
    /**
     * Rotates this tetromino counterclockwise.
     */
    public void rotateLeft() {
        if(rotations == 0)
            return;
        
        rotation++;
        if(rotation == 4)
            rotation = 0;
        
        rotations--;
    }
}
