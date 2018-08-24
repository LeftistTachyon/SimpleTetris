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
     * Rotates this tetromino clockwise.
     */
    public void rotateClockwise() {
        rotation--;
        if(rotation == -1) 
            rotation = 3;
    }
    
    /**
     * Rotates this tetromino counterclockwise.
     */
    public void rotateCounterclockwise() {
        rotation++;
        if(rotation == 4) 
            rotation = 0;
    }
}
