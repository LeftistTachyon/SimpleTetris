package simpletetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import simpletetris.TetrisKeyAdapter.GameAction;
import static simpletetris.TetrisKeyAdapter.GameAction.*;

/**
 * A class that represents the Tetris matrix
 * @author Jed Wang
 */
public class TetrisMatrix {
    /**
     * The currently falling tetromino
     */
    private Tetromino falling;
    
    /**
     * The RNG for which piece is coming next
     */
    private TetrisBag bag;
    
    /**
     * The "x" coordinate of the top left corner of the Tetromino's box
     */
    private int x;
    
    /**
     * The "y" coordinate of the top left corner of the Tetromino's box
     */
    private int y;
    
    /**
     * A {@code Color[][]} that stores all of the minos in the matrix.
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
     * The visible height of the matrix; playing field
     */
    public static final double VISIBLE_HEIGHT = 21;
    
    /**
     * Creates a new TetrisMatrix.
     */
    public TetrisMatrix() {
        matrix = new Color[WIDTH][HEIGHT];
        bag = new TetrisBag();
        newPiece();
    }
    
    /**
     * Draws this TetrisMatrix
     * @param g2D the Graphics2D to draw with
     */
    public void draw(Graphics2D g2D) {
        g2D.translate(15, 15-Mino.MINO_WIDTH*VISIBLE_HEIGHT);
        
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == null) 
                    continue;
                Mino.drawMino(i*Mino.MINO_WIDTH, j*Mino.MINO_WIDTH, 
                        matrix[i][j], g2D);
            }
        }
        
        int tlx = x*Mino.MINO_WIDTH, tly = y*Mino.MINO_WIDTH;
        Color[][] tetro = falling.getDrawBox();
        for(int i = 0; i < tetro.length; i++) {
            for(int j = 0; j < tetro[i].length; j++) {
                if(tetro[i][j] == null) 
                    continue;
                Mino.drawMino(tlx + i*Mino.MINO_WIDTH, tly + j*Mino.MINO_WIDTH, 
                        tetro[i][j], g2D);
            }
        }
    }
    
    /**
     * Returns the section of the matrix that the falling piece occupies<br>
     * e.g.<br>
     * Falling piece (a TetO) is at (0, 0).<br>
     * The matrix looks as such: <br>
     * <code>....<br>
     * X.XX<br>
     * XXXX<br></code><br>
     * The output would be:<br>
     * <code>..<br>
     * X.</code>
     * @return the section of the matrix that the falling piece occupies
     */
    public Color[][] miniMatrix() {
        return miniMatrix(0, 0);
    }
    
    /**
     * Returns the section of the matrix that the falling piece occupies, 
     * considering the offset.<br>
     * e.g. <code>(1, -1)</code> would kick the piece right 1 and down 1
     * @see TetrisMatrix#miniMatrix()
     * @param offsetX the offset of the X coordinate
     * @param offsetY the offset of the Y coordinate
     * @return the section of the matrix that the falling piece occupies 
     * (not really)
     */
    public Color[][] miniMatrix(int offsetX, int offsetY) {
        Color[][] output = new Color[falling.getRotationBoxWidth()]
                [falling.getRotationBoxWidth()];
        
        int tlx = x + offsetX, tly = y - offsetY;
        for(int i = 0; i < output.length; i++) {
            int trueX = tlx + i;
            for(int j = 0; j < output[i].length; j++) {
                int trueY = tly + j;
                if(trueX < 0 || trueX >= WIDTH || trueY < 0 || trueY >= HEIGHT) {
                    output[i][j] = Color.BLACK;
                } else {
                    output[i][j] = matrix[trueX][trueY];
                }
            }
        }
        
        return output;
    }
    
    /**
     * Executes the given action.
     * @param ga the action to execute.
     */
    public void executeAction(GameAction ga) {
        System.out.println(ga.name());
        switch(ga) {
            case ROTATE_LEFT:
                Point kickL = falling.getWallKick(this, 
                        Tetromino.COUNTERCLOCKWISE);
                falling.rotateLeft();
                x += kickL.x;
                y -= kickL.y;
                break;
            case ROTATE_RIGHT:
                Point kickR = falling.getWallKick(this, 
                        Tetromino.CLOCKWISE);
                falling.rotateRight();
                x += kickR.x;
                y -= kickR.y;
                break;
            case MOVE_LEFT:
                if(!falling.overlaps(miniMatrix(-1, 0))) x--;
                break;
            case MOVE_RIGHT:
                if(!falling.overlaps(miniMatrix(1, 0))) x++;
                break;
            case SOFT_DROP:
                if(!falling.overlaps(miniMatrix(0, -1))) y++;
                break;
            case HARD_DROP:
                int placeHolderY = 0;
                while(!falling.overlaps(miniMatrix(0, -placeHolderY))){
                    placeHolderY++;
                }
                y += placeHolderY - 1;
                lockPiece();
                break;
        }
    }
    
    /**
     * Chooses the next piece and places it at the top of the playfield.
     */
    public void newPiece() {
        falling = bag.remove();
        
        y = 21;
        // y = 40 - falling.getRotationBoxWidth();
        x = (WIDTH - falling.getRotationBoxWidth())/2;
        
        if(falling.overlaps(miniMatrix())){
            // Game over!
            System.exit(1);
        }
    }
    
    /**
     * Locks all pieces and does some checks before sending in a new piece.
     */
    public void lockPiece() {
        // lock
        Color[][] copy = falling.getDrawBox();
        for(int r= 0; r < copy.length; r++) {
            for(int c = 0; c < copy[r].length; c++) {
                if(copy[r][c] != null && matrix[r + x][c + y] == null) {
                    matrix[r + x][c + y] = copy[r][c];
                }
            }
        }
        
        // remove lines
        
        
        // after locking, reset
        newPiece();
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
    
    /**
     * Determines whether a line is completely filled
     * @param row which row to check
     * @return 
     */
    public boolean lineFilled(int row) {
        
    }

    /**
     * Returns the currently falling tetromino
     * @return the currently falling tetromino
     */
    public Tetromino getFallingPiece() {
        return falling;
    }
    
    /**
     * Prints the matrix.
     */
    public void printMatrix() {
        for(Color[] colors : matrix) {
            for(Color color : colors) {
                if(color == null) {
                    System.out.print(" ");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Prints the mini-matrix offsetted from the falling tetromino
     * @param offsetX the offset for the x coordinate
     * @param offsetY the offset for the y coordinate
     */
    public void printDebugMatrix(int offsetX, int offsetY) {
        Color[][] mini = miniMatrix(offsetX, offsetY), tet = falling.getDrawBox();
        for(int i = 0;i<mini.length;i++) {
            for(int j = 0;j<mini[i].length;j++) {
                if(mini[i][j] == null && tet[i][j] == null) {
                    System.out.print(" ");
                } else if(mini[i][j] == null ^ tet[i][j] == null) {
                    System.out.print("@");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
}
