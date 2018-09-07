package simpletetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import simpletetris.TetrisKeyAdapter.GameAction;
import static simpletetris.TetrisKeyAdapter.GameAction.*;
import static simpletetris.Mino.*;

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
     * Stores whether a piece can swap to hold.
     */
    private boolean holdSwappable;
    /**
     * The tetromino that is currently being held.
     */
    private Tetromino hold;
    
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
    public static final double VISIBLE_HEIGHT = 20.5;
    
    /**
     * A block of the background
     */
    private static final BufferedImage BACKGROUND_BLOCK;
    
    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new File("images/background_block.png"));
        } catch (IOException ex) {
            System.err.println("Background block image file not found");
        }
        BACKGROUND_BLOCK = temp;
    }
    
    /**
     * Creates a new TetrisMatrix.
     */
    public TetrisMatrix() {
        hold = null;
        matrix = new Color[WIDTH][HEIGHT];
        bag = new TetrisBag();
        newPiece();
    }
    
    /**
     * Draws this TetrisMatrix
     * @param g2D the Graphics2D to draw with
     */
    public void draw(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_MITER));
        
        g2D.translate(15, 0);
        g2D.setColor(Color.BLACK);
        g2D.setFont(new Font("Consolas", 0, 36));
        g2D.drawString("HOLD", 15, 45);
        
        if(hold != null) {
            int miniLength = 100 / hold.getRotationBoxWidth();
            Color[][] tetra = hold.getDrawBox();
            for (int i = 0, x = 5; i < tetra.length; i++, x += miniLength) {
                for (int j = 0, y = 55; j < tetra[i].length; j++, y += miniLength) {
                    if (tetra[i][j] == null) {
                        continue;
                    }
                    Mino.drawMiniMino(x, y, miniLength, tetra[i][j], g2D);
                }
            }
        }
        
        g2D.drawRect(0, 50, 110, 110);
        
        
        
        g2D.translate(110, -MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT));
        
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                
                if(matrix[i][j] == null) 
                    continue;
                drawMino(i*MINO_WIDTH, j*MINO_WIDTH, 
                        matrix[i][j], g2D);
            }
        }
        
        int tlx = x*MINO_WIDTH, tly = y*MINO_WIDTH, 
                tlGy = getGhostY()*MINO_WIDTH;
        g2D.setColor(falling.getColor());
        Color[][] tetro = falling.getDrawBox();
        for(int i = 0; i < tetro.length; i++) {
            for(int j = 0; j < tetro[i].length; j++) {
                if(tetro[i][j] == null) 
                    continue;
                g2D.drawRect(tlx + i*MINO_WIDTH + 5, tlGy + j*MINO_WIDTH + 5, 
                        MINO_WIDTH - 10, MINO_WIDTH - 10);
                drawMino(tlx + i*MINO_WIDTH, tly + j*MINO_WIDTH, 
                        tetro[i][j], g2D);
            }
        }
        
        g2D.setColor(Color.BLACK);
        g2D.drawRect(0, (int) (MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT)), 
                WIDTH*MINO_WIDTH, (int) (VISIBLE_HEIGHT*MINO_WIDTH));
        
        g2D.translate(MINO_WIDTH*WIDTH, MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT));
        g2D.drawString("NEXT", 15, 45);
        
        if(bag.next(0) != null) {
            int miniLength = 100 / bag.next(0).getRotationBoxWidth();
            Color[][] tetra = bag.next(0).getDrawBox();
            for (int i = 0, x = 5; i < tetra.length; i++, x += miniLength) {
                for (int j = 0, y = 55; j < tetra[i].length; j++, y += miniLength) {
                    if (tetra[i][j] == null) {
                        continue;
                    }
                    Mino.drawMiniMino(x, y, miniLength, tetra[i][j], g2D);
                }
            }
        }
        g2D.drawRect(0, 50, 110, 110);
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
                if(kickL == null) return;
                falling.rotateLeft();
                x += kickL.x;
                y -= kickL.y;
                break;
            case ROTATE_RIGHT:
                Point kickR = falling.getWallKick(this, 
                        Tetromino.CLOCKWISE);
                if(kickR == null) return;
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
                y = getGhostY();
                lockPiece();
                break;
            case HOLD:
                if(holdSwappable) {
                    if(hold == null) {
                        hold = falling;
                        newPiece();
                    } else {
                        Tetromino temp = hold;
                        hold = falling;
                        falling = temp;
                        
                        falling.resetRotationCount();
                        hold.rotateTo(Tetromino.UP);
                        
                        y = 20;
                        x = (WIDTH - falling.getRotationBoxWidth())/2;
                    }
                    
                    holdSwappable = false;
                }
                break;
        }
    }
    
    /**
     * Chooses the next piece and places it at the top of the playfield.
     */
    public void newPiece() {
        falling = bag.remove();
        
        y = 20;
        x = (WIDTH - falling.getRotationBoxWidth())/2;
        
        if(falling.overlaps(miniMatrix())){
            // Game over!
            System.exit(0);
        }
        
        holdSwappable = true;
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
        for(int i = 0; i < HEIGHT; i++) {
            if(lineFilled(i)) {
                for(int j = i; j >= 1; j--) {
                    clearLine(j);
                }
                emptyLine(0);
            }
        }
        
        // after locking, reset
        newPiece();
    }
    
    /**
     * Determines the y-coordinate of the ghost-piece
     * @return the y-coordinate of the ghost-piece
     */
    public int getGhostY() {
        int placeHolderY = 0;
        while(!falling.overlaps(miniMatrix(0, -placeHolderY))){
            placeHolderY++;
        }
        return y + placeHolderY - 1;
    }
    
    /**
     * Empties a row of blocks
     * @param row which row to empty
     */
    public void emptyLine(int row) {
        for(int i = 0; i < WIDTH; i++) {
            matrix[i][row] = null;
        }
    }
    
    /**
     * Clears a line in the matrix
     * @param row which row to clear
     */
    public void clearLine(int row) {
        for(int i = 0; i < WIDTH; i++) {
            matrix[i][row] = matrix[i][row - 1];
        }
    }
    
    /**
     * Determines whether a line is completely filled
     * @param row which row to check
     * @return whether a line is completely filled
     */
    public boolean lineFilled(int row) {
        for(int i = 0; i < WIDTH; i++) {
            if(matrix[i][row] == null) 
                return false;
        }
        return true;
    }
    
    /**
     * Outputs a line of the matrix
     * @param row which row to print
     */
    public void printLine(int row) {
        for(int i = 0; i < WIDTH; i++) {
            System.out.print((matrix[i][row] == null)?" ":"X");
        }
        System.out.println();
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
