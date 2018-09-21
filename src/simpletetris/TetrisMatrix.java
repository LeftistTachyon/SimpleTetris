package simpletetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private final ScoreKeeper sk;
    
    /**
     * The GarbageDealer for this TetrisMatrix.
     */
    private final GarbageDealer gd;
    
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
    private final Gravity gravity;
    
    /**
     * Adding lock delay for tetrominos
     */
    private final LockDelay lockDelay;
    
    /**
     * All of the listeners
     */
    private ArrayList<ActionListener> listeners = null;
    
    /**
     * Controls the line clear animation
     */
    private LinkedList<Integer> rowsCleared;
    
    /*
     * Controls the line clear animation
     */
    private double clearAnimation;
    
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
        gd = new GarbageDealer();
        sk.addListener((ActionEvent e) -> {
            System.out.println(e.getActionCommand());
            String garbageToSend = gd.counterGarbage(e.getActionCommand());
            if(garbageToSend != null) 
                System.out.println("SEND" + garbageToSend);
        });
        gd.addGarbage("4 2 3");
        
        rowsCleared = null;
        
        gravity = new Gravity();
        lockDelay = new LockDelay();
        
        kicked = false;
        hold = null;
        matrix = new Color[WIDTH][HEIGHT];
        bag = new TetrisBag();
        newPiece();
        
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(gravity, 0, 10, TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(lockDelay, 0, 10, TimeUnit.MILLISECONDS);
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
        
        if(falling != null) {
            int tlx = x * MINO_WIDTH, tly = y * MINO_WIDTH,
                    tlGy = getGhostY() * MINO_WIDTH;
            g2D.setColor(falling.getColor());
            Color[][] tetro = falling.getDrawBox();
            for (int i = 0; i < tetro.length; i++) {
                for (int j = 0; j < tetro[i].length; j++) {
                    if (tetro[i][j] == null) {
                        continue;
                    }
                    g2D.drawRect(tlx + i * MINO_WIDTH + 7, tlGy + j * MINO_WIDTH + 7,
                            MINO_WIDTH - 14, MINO_WIDTH - 14);
                    drawMino(tlx + i * MINO_WIDTH, tly + j * MINO_WIDTH,
                            tetro[i][j], g2D);
                }
            }
        }
        
        if(rowsCleared != null) {
            if(clearAnimation <= -255) {
                for(int i:rowsCleared) {
                    for (int j = i; j >= 1; j--) {
                        clearLine(j);
                    }
                    emptyLine(0);
                }
                
                rowsCleared = null;
                
                // gravity.enable();
                
                addGarbage();
                
                newPiece();
            } else {
                Color whitish = new Color(255, 255, 255, (clearAnimation >= 0) 
                        ? ((clearAnimation <= 255) ? (int) clearAnimation : 255) 
                        : 0);
                g2D.setStroke(new BasicStroke());
                g2D.setColor(whitish);
                for(int row:rowsCleared) {
                    int yPos = MINO_WIDTH * row;
                    g2D.fillRect(0, yPos, MINO_WIDTH * WIDTH, MINO_WIDTH);
                }

                clearAnimation -= 25.5;
                
                g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_MITER));
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
        if(falling == null) return;
        switch(ga) {
            case ROTATE_LEFT:
                Point kickL = falling.getWallKick(this, 
                        Tetromino.COUNTERCLOCKWISE);
                if(kickL == null) return;
                falling.rotateLeft();
                x += kickL.x;
                y -= kickL.y;
                kicked = kickL.x != 0 || kickL.y != 0;
                if(kicked || (falling instanceof TetT && immobile())) {
                    lockDelay.addTouch();
                }
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
                if(kicked || (falling instanceof TetT && immobile())) {
                    lockDelay.addTouch();
                }
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
                        
                        moveTetToStart();
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
        
        moveTetToStart();
        
        gravity.resetGravity();
        
        holdSwappable = true;
    }
    
    /**
     * Moves the tetromino to the start
     */
    private void moveTetToStart() {
        y = 20;
        x = (WIDTH - falling.getRotationBoxWidth())/2;
        
        for(int i = 0; i < 2 /* The leeway is by 2 */; i++) {
            if(falling.overlaps(miniMatrix(0, -2)))
                y--;
        }
        if(falling.overlaps(miniMatrix())){
            // no falling piece
            falling = null;
            
            // Game over!
            notifyListeners("GAMEOVER");
        }
        if(falling.overlaps(miniMatrix(0, -1))) {
            // ditch the piece first
            Color[][] copy = falling.getDrawBox();
            for(int r= 0; r < copy.length; r++) {
                for(int c = 0; c < copy[r].length; c++) {
                    if(copy[r][c] != null && matrix[r + x][c + y] == null) {
                        matrix[r + x][c + y] = copy[r][c];
                    }
                }
            }
            
            // no falling piece
            falling = null;
            
            // Game over!
            notifyListeners("GAMEOVER");
        }
    }
    
    /**
     * Locks all pieces and does some checks before sending in a new piece.
     */
    public void lockPiece() {
        // stop gravity
        gravity.disable();
        
        // reset lock piece checker
        lockDelay.reset();
        
        // determine immobility
        boolean immobile = immobile();
        
        // lock
        Color[][] copy = falling.getDrawBox();
        for(int r= 0; r < copy.length; r++) {
            for(int c = 0; c < copy[r].length; c++) {
                if(copy[r][c] != null && matrix[r + x][c + y] == null) {
                    matrix[r + x][c + y] = copy[r][c];
                }
            }
        }
        
        int linesCleared = 0;
        for(int i = 0; i < HEIGHT; i++) {
            if(lineFilled(i)) {
                linesCleared++;
            }
        }
        
        // check for t-spins
        if(falling instanceof TetT && (lastAction == ROTATE_LEFT || 
                lastAction == ROTATE_RIGHT) && threeCorner()) {
            System.out.println("T-spin " + linesCleared);
            if(!immobile && kicked && linesCleared < 2) {
                sk.newLinesCleared(linesCleared, ScoreKeeper.T_SPIN_MINI, allClear());
            } else {
                sk.newLinesCleared(linesCleared, ScoreKeeper.T_SPIN, allClear());
            }
        } else {
            sk.newLinesCleared(linesCleared, ScoreKeeper.NORMAL, allClear());
        }
        
        // empty lines
        for(int i = 0; i < HEIGHT; i++) {
            if(lineFilled(i)) {
                if(rowsCleared == null) rowsCleared = new LinkedList<>();
                emptyLine(i);
                rowsCleared.add(i);
            }
        }
        
        // after locking, reset
        if(linesCleared == 0) {
            // add garbage
            addGarbage();
            newPiece();
        } else {
            falling = null;
            clearAnimation = 510;
        }
    }
    
    /**
     * Adds garbage needed for this drop
     */
    private void addGarbage() {
        int temp = 0;
        while(true) {
            int temptemp = gd.peekNextGarbage();
            if(temptemp == 0) return;
            temp += temptemp;
            if(temp > 5) return;
            addGarbageLines(gd.getNextGarbage());
            System.out.println("Oof! " + temptemp + " lines of garbage");
        }
    }
    
    /**
     * Determines if the board is all clear
     * @return if the board is all clear
     */
    private boolean allClear() {
        for(int i = 0; i < HEIGHT; i++) {
            if(!lineHomogenous(i))
                return false;
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
     * Determines whether the t-piece has 3 of the 4 corners of the 
     * bounding box are filled in.
     * @return whether 3-corners are filled
     */
    private boolean threeCorner() {
        if(!(falling instanceof TetT)) 
            throw new IllegalStateException("Cannot perform a 3-corner check on a non-T tetromino");
        
        // box is 3x3
        Color[][] box = miniMatrix();
        int cnt = 0;
        if(box[0][0] != null) cnt++;
        if(box[0][2] != null) cnt++;
        if(box[2][0] != null) cnt++;
        if(box[2][2] != null) cnt++;
        
        return cnt >= 3;
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
     * Pushes a line upwards.
     * @param row which row to push upwards
     * @param rows how many lines to push up the given line
     */
    public void pushUpLine(int row, int rows) {
        for(int i = 0; i < WIDTH; i++) {
            matrix[i][row - rows] = matrix[i][row];
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
     * Determines whether a line is completely empty
     * @param row which row to check
     * @return whether a line is completely empty
     */
    public boolean lineEmpty(int row) {
        for(int i = 0; i < WIDTH; i++) {
            if(matrix[i][row] != null)
                return false;
        }
        return true;
    }
    
    /**
     * Determines whether a row is either all filled or all empty<br>
     * Used to determine whether an all-clear has occured
     * @param row which row to check
     * @return whether a row is either all filled or all empty
     */
    public boolean lineHomogenous(int row) {
        boolean empty = matrix[0][row] == null;
        return (empty)?lineEmpty(row):lineFilled(row);
    }
    
    /**
     * Adds a given amount of garbage lines to the bottom of the matrix.
     * @param lines how many garbage lines to add
     */
    public void addGarbageLines(int lines) {
       for(int i = lines; i < HEIGHT; i++) {
           pushUpLine(i, lines);
       }
       
        for(int i = 0; i < lines; i++) {
            emptyLine(HEIGHT - i - 1);
        }
       
       int hole = (int) (Math.random() * WIDTH);
       for(int i = 0; i < lines; i++) {
           setGarbageLine(HEIGHT - i - 1, hole);
       }
    }
    
    /**
     * Sets a row as a garbage line
     * @param row which row to set
     * @param hole the column the hole should be at
     */
    private void setGarbageLine(int row, int hole) {
        for(int i = 0; i < WIDTH; i++) {
            if(i != hole) matrix[i][row] = Color.GRAY;
        }
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
    
    /**
     * Adds an ActionListener to listen to this TetrisMatrix
     * @param al the ActionListener to add
     */
    public void addActionListener(ActionListener al) {
        if(listeners == null) listeners = new ArrayList<>();
        listeners.add(al);
    }
    
    /**
     * Clears all listeners.
     */
    public void clearListeners() {
        listeners = null;
    }
    
    /**
     * Notifies listeners of a message / occurance
     * @param message the message to send
     */
    private void notifyListeners(String message) {
        if(listeners == null) return;
        ActionEvent ae = new ActionEvent(this, 0, message);
        for(ActionListener listener : listeners) {
            listener.actionPerformed(ae);
        }
    }
    
    /**
     * Disables gravity.
     */
    public void disableGravity() {
        gravity.disable();
    }
    
    /**
     * Enables gravity.
     */
    public void enableGravity() {
        gravity.enable();
    }
    
    /**
     * Pauses gravity.
     */
    public void pauseGravity() {
        gravity.pause();
    }
    
    /**
     * Resumes gravity.
     */
    public void resumeGravity() {
        gravity.resume();
    }
    
    /**
     * Adds gravity to the pieces
     */
    private class Gravity implements Runnable {
        /**
         * Set to {@code false} to stop gravity
         */
        private boolean enabled = true;
        
        /**
         * Whether this Gravity is paused
         */
        private boolean paused = false;
        
        /**
         * The counter
         */
        private int i = 0;
        
        @Override
        public void run() {
            try {
                /*if(cnt > 1) {
                    System.out.println(cnt + "/60 G");
                }*/
                if(falling != null) {
                    if (paused) {
                        if(falling == null) return;
                        if (falling.overlaps(miniMatrix(0, -1)) && enabled) {
                            enabled = false;
                        }
                        if(falling == null) return;
                        if (!falling.overlaps(miniMatrix(0, -1)) && !enabled) {
                            enabled = true;
                        }
                        return;
                    }
                    if (!enabled) {
                        i = 0;
                    } else if (i == 99) {
                        y++;
                        lastAction = GRAVITY;
                    }
                    if (falling.overlaps(miniMatrix(0, -1)) && enabled) {
                        enabled = false;
                    }
                    if (!falling.overlaps(miniMatrix(0, -1)) && !enabled) {
                        enabled = true;
                    }
                    i++;
                    i %= 100;
                }
            } catch (Exception e) {
                // Just in case stuff happens
                e.printStackTrace();
            }
        }
        
        /**
         * Stops gravity.
         */
        public void disable() {
            enabled = false;
        }
        
        /**
         * Restarts gravity.
         */
        public void enable() {
            enabled = true;
        }
        
        /**
         * Pauses gravity.
         */
        public void pause() {
            paused = true;
        }
        
        /**
         * Resumes gravity.
         */
        public void resume() {
            paused = false;
        }
        
        /**
         * Resets gravity so the next piece is affected correctly
         */
        public void resetGravity() {
            i = 0;
        }
    }
    
    /**
     * A class that deals with lock delay.
     */
    private class LockDelay implements Runnable {
        /**
         * The number of times this tetromino touched a 
         * thing with its bottom surface.
         */
        private int touches;
        
        /**
         * Whether this tetromino is floating
         */
        private boolean floating;
        
        /**
         * The piece number dropped
         */
        private long pieceNo;

        /**
         * Creates a new LockDelay.
         */
        public LockDelay() {
            pieceNo = 0;
            touches = 0;
            floating = (falling == null) ? false 
                    : !falling.overlaps(miniMatrix(0, -1));
        }

        @Override
        public void run() {
            try {
                if(falling != null) {
                    if (falling.overlaps(miniMatrix(0, -1)) && floating) {
                        floating = false;
                        touches++;
                        Executors.newScheduledThreadPool(1).schedule(
                                new LockDelayChecker(touches, pieceNo),
                                500, TimeUnit.MILLISECONDS);
                    }
                    if (!falling.overlaps(miniMatrix(0, -1)) && !floating) {
                        floating = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Resets this LockDelay so that checks are not run for this piece anymore.
         */
        public void reset() {
            touches = 0;
            floating = !falling.overlaps(miniMatrix(0, -1));
            pieceNo++;
        }
        
        /**
         * Adds a touch.
         */
        public void addTouch() {
            touches++;
            Executors.newScheduledThreadPool(1).schedule(
                    new LockDelayChecker(touches, pieceNo), 
                    500, TimeUnit.MILLISECONDS);
        }
        
        /**
         * Checks if the falling tetromino should be locked
         */
        private class LockDelayChecker implements Runnable {
            /**
             * The number of touches the tetromino had at that moment
             */
            private final int touches_;
            
            /**
             * The piece id of the piece that this is detecting
             */
            private final long pieceNo_;

            /**
             * Creates a new LockDelayChecker
             * @param touches 
             */
            public LockDelayChecker(int touches, long pieceNo) {
                touches_ = touches;
                pieceNo_ = pieceNo;
            }
            
            @Override
            public void run() {
                if(!floating && touches_ == touches && pieceNo_ == pieceNo) {
                    lockPiece();
                }
            }
        }
    }
}
