package simpletetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * The ScoreKeeper for this TetrisMatrix.
     */
    private ScoreKeeper sk;
    
    /**
     * Determines whether the piece was just kicked
     */
    private boolean kicked;
    
    /**
     * The last action performed by the player
     */
    private GameAction lastAction;
    
    /**
     * Adding gravity
     */
    private Gravity gravity;
    
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
    
    /**
     * The background image
     */
    private static final BufferedImage BACKGROUND_IMAGE;
    
    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new File("images/background_block.png"));
        } catch (IOException ex) {
            System.err.println("Background block image file not found");
        }
        BACKGROUND_BLOCK = temp;
        
        temp = null;
        try {
            temp = ImageIO.read(new File("images/background.jpg"));
        } catch (IOException ex) {
            System.err.println("Background image file not found");
        }
        BACKGROUND_IMAGE = temp;
    }
    
    /**
     * Creates a new TetrisMatrix.
     */
    public TetrisMatrix() {
        sk = new ScoreKeeper();
        kicked = false;
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
            BufferedImage miniImage = hold.getMiniImage();
            g2D.drawImage(miniImage, 5, 55, 100, 60, null);
        }
        
        g2D.drawRect(0, 50, 110, 70);
        
        g2D.translate(110, 0);
        g2D.setClip(0, 0, MINO_WIDTH*WIDTH, 
                (int) (MINO_WIDTH*VISIBLE_HEIGHT));
        g2D.drawImage(BACKGROUND_IMAGE, 0, 0, 
                MINO_WIDTH*WIDTH, (int) (MINO_WIDTH*VISIBLE_HEIGHT), null);
        
        g2D.translate(0, -MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT));
        
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                
                if(matrix[i][j] == null) {
                    g2D.drawImage(BACKGROUND_BLOCK, null, 
                            i*MINO_WIDTH, j*MINO_WIDTH);
                } else {
                    drawMino(i*MINO_WIDTH, j*MINO_WIDTH, 
                            matrix[i][j], g2D);
                }
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
                g2D.drawRect(tlx + i*MINO_WIDTH + 7, tlGy + j*MINO_WIDTH + 7, 
                        MINO_WIDTH - 14, MINO_WIDTH - 14);
                drawMino(tlx + i*MINO_WIDTH, tly + j*MINO_WIDTH, 
                        tetro[i][j], g2D);
            }
        }
        
        g2D.setClip(null);
        g2D.setColor(Color.BLACK);
        g2D.drawRect(0, (int) (MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT)), 
                WIDTH*MINO_WIDTH, (int) (VISIBLE_HEIGHT*MINO_WIDTH));
        
        g2D.translate(MINO_WIDTH*WIDTH, MINO_WIDTH*(HEIGHT - VISIBLE_HEIGHT));
        g2D.drawString("NEXT", 15, 45);
        
        if(bag.next(0) != null) {
            BufferedImage miniImage = bag.next(0).getMiniImage();
            g2D.drawImage(miniImage, 5, 55, 100, 60, null);
        }
        g2D.drawRect(0, 50, 110, 70);
        
        for(int i = 1; i < 3; i++) {
            if(bag.next(i) != null) {
                BufferedImage miniImage = bag.next(i).getMiniImage();
                g2D.drawImage(miniImage, 25, 75 + 85*i, 80, 48, null);
            }
            g2D.drawRect(20, 70 + 85*i, 90, 58);
        }
        
        g2D.drawString("" + sk.getLinesSent(), -450, 200);
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
                kicked = kickL.x != 0 || kickL.y != 0;
                lastAction = ga;
                break;
            case ROTATE_RIGHT:
                Point kickR = falling.getWallKick(this, 
                        Tetromino.CLOCKWISE);
                if(kickR == null) return;
                falling.rotateRight();
                x += kickR.x;
                y -= kickR.y;
                kicked = kickR.x != 0 || kickR.y != 0;
                lastAction = ga;
                break;
            case MOVE_LEFT:
                if(!falling.overlaps(miniMatrix(-1, 0))) x--;
                lastAction = ga;
                break;
            case MOVE_RIGHT:
                if(!falling.overlaps(miniMatrix(1, 0))) x++;
                lastAction = ga;
                break;
            case SOFT_DROP:
                if(!falling.overlaps(miniMatrix(0, -1))) y++;
                lastAction = ga;
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
                        
                        falling.rotateTo(Tetromino.UP);
                        falling.resetRotationCount();
                        hold.rotateTo(Tetromino.UP);
                        
                        y = 20;
                        x = (WIDTH - falling.getRotationBoxWidth())/2;
                    }
                    
                    holdSwappable = false;
                }
                break;
        }
        if(gravity.stop && !falling.overlaps(miniMatrix(0, -1))) {
            gravity = new Gravity();
            new Thread(gravity).start();
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
        
        gravity = new Gravity();
        new Thread(gravity).start();
        
        holdSwappable = true;
    }
    
    /**
     * Locks all pieces and does some checks before sending in a new piece.
     */
    public void lockPiece() {
        // stop gravity
        gravity.stop();
        
        if(falling instanceof TetT) {
            if(falling.overlaps(miniMatrix(1, 0))) {
                System.out.println("RIGHT");
                printDebugMatrix(1, 0);
            }
            if(falling.overlaps(miniMatrix(-1, 0))) {
                System.out.println("LEFT");
                printDebugMatrix(-1, 0);
            }
            if(falling.overlaps(miniMatrix(0, 1))) {
                System.out.println("UP");
                printDebugMatrix(0, 1);
            }
            if(falling.overlaps(miniMatrix(0, -1))) {
                System.out.println("DOWN");
                printDebugMatrix(0, -1);
            }
        }
        
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
        int linesCleared = 0;
        for(int i = 0; i < HEIGHT; i++) {
            if(lineFilled(i)) {
                for(int j = i; j >= 1; j--) {
                    clearLine(j);
                }
                linesCleared++;
                emptyLine(0);
            }
        }
        
        // check for t-spins
        if(falling instanceof TetT && (lastAction == GameAction.ROTATE_LEFT || 
                lastAction == GameAction.ROTATE_RIGHT) && immobile()) {
            System.out.println("T-spin " + linesCleared);
            if(kicked && linesCleared < 2) {
                sk.newLinesCleared(linesCleared, ScoreKeeper.T_SPIN_MINI, allClear());
            } else {
                sk.newLinesCleared(linesCleared, ScoreKeeper.T_SPIN, allClear());
            }
        } else {
            sk.newLinesCleared(linesCleared, ScoreKeeper.NORMAL, allClear());
        }
        
        // after locking, reset
        newPiece();
    }
    
    /**
     * Determines if the board is all clear
     * @return if the board is all clear
     */
    private boolean allClear() {
        for(Color[] colors : matrix) {
            for(Color color : colors) {
                if(color != null) return false;
            }
        }
        return true;
    }
    
    /**
     * Determines whether the piece is immobile
     * @return whether the piece is immobile
     */
    private boolean immobile() {
        return falling.overlaps(miniMatrix(1, 0)) 
                && falling.overlaps(miniMatrix(-1, 0)) 
                && falling.overlaps(miniMatrix(0, 1)) 
                && falling.overlaps(miniMatrix(0, -1));
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
                if(mini[j][i] == null && tet[j][i] == null) {
                    System.out.print(" ");
                } else if(mini[j][i] == null ^ tet[j][i] == null) {
                    System.out.print("@");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
    
    private static int cnt = 0;
    
    /**
     * Adds gravity to the pieces
     */
    private class Gravity implements Runnable {
        /**
         * Set to {@code true} to stop gravity
         */
        private boolean stop = false;
        
        @Override
        public void run() {
            cnt++;
            for(int i = 0; !stop && 
                    !falling.overlaps(miniMatrix(0, -1)); i++, i %= 100) {
                /*if(cnt > 1) {
                    System.out.println(cnt + "/60 G");
                }*/
                if(i == 99) y++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    System.err.println("Gravity thread interrupted");
                }
            }
            cnt--;
        }
        
        /**
         * Stops gravity.
         */
        public void stop() {
            stop = true;
        }
    }
}
