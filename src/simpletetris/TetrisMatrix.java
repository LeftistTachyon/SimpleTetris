package simpletetris;

import java.awt.Color;
import java.awt.Graphics2D;
import simpletetris.TetrisKeyAdapter.GameAction;
import static simpletetris.TetrisKeyAdapter.GameAction.*;

/**
 * A class that represents the Tetris matrix
 * @author Jed Wang
 */
public class TetrisMatrix {
    /**
     * The falling tetromino
     */
    private Tetromino falling;
    
    /**
     * The matrix.
     */
    private Color[][] matrix;
    
    /**
     * The width of the matrix
     */
    public static final int WIDTH = 10;
    
    /**
     * The height of the matrix
     */
    public static final int HEIGHT = 40;
    
    /**
     * Creates a new TetrisMatrix.
     */
    public TetrisMatrix() {
        matrix = new Color[WIDTH][HEIGHT];
        falling = new TetO();
    }
    
    /**
     * Draws this TetrisMatrix
     * @param g2D the Graphics2D to draw with
     */
    public void draw(Graphics2D g2D) {
        
    }
    
    /**
     * Executes the given action.
     * @param ga the action to execute.
     */
    public void executeAction(GameAction ga) {
        switch(ga) {
            case ROTATE_LEFT:
                break;
            case ROTATE_RIGHT:
                break;
            case MOVE_LEFT:
                break;
            case MOVE_RIGHT:
                break;
        }
    }
    
    /**
     * Locks all pieces and does some checks before sending in a new piece.
     */
    public void lockPieces() {
        falling = new TetO();
    }
    
    /**
     * Empties a row of blocks
     * @param row which row to empty
     */
    public void emptyLine(int row) {
        for(int i = 0; i < matrix[row].length; i++) {
            matrix[row][i] = null;
        }
    }
    
    /**
     * Clears a line in the matrix
     * @param row which row to clear
     */
    public void clearLine(int row) {
        for(int i = row; i>0; i++) {
            for(int j = 0; j < WIDTH; j++) {
                matrix[i][j] = matrix[i-1][j];
            }
        }
    }
}