package simpletetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The JPanel where all of the images are drawn
 * @author Jed Wang
 */
public class TetrisPanel extends JPanel implements Runnable {
    /**
     * The matrix that the player manipulates.
     */
    public final TetrisMatrix playerMatrix;
    
    /**
     * The score of this player.
     */
    private int playerScore;
    
    /**
     * The matrix that the opponent manipulates.
     */
    public final TetrisMatrix opponentMatrix;
    
    /**
     * The score of the opponent.
     */
    private int opponentScore;
    
    /**
     * Best of {@code FIRST_TO*2-1}. In this case, best of 3.
     */
    public static final int FIRST_TO = 2;
    
    /**
     * The image for the score-keeper / win-tracker.
     */
    private static final BufferedImage WIN_TRACKER;
    
    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new File("images/winTracker" + FIRST_TO + ".png"));
        } catch (IOException ex) {
            System.err.println("Win tracker image file not found");
        }
        WIN_TRACKER = temp;
    }
    
    /**
     * Whether to stop
     */
    private boolean stop = false;

    /**
     * Creates a new TetrisPanel.
     */
    public TetrisPanel() {
        playerScore = 0; 
        opponentScore = 0;
        playerMatrix = new TetrisMatrix(true);
        opponentMatrix = new TetrisMatrix(false);
        
        startGame();
        
        playerMatrix.addActionListener((ActionEvent e) -> {
            String command = e.getActionCommand();
            if(command.equals("GAMEOVER")) {
                opponentScore++;
                if(opponentScore == 2) {
                    playerMatrix.clearFalling();
                    opponentMatrix.clearFalling();
                    notifyListeners("MATCHOVERfalse");
                }
                System.out.println("You lose. :(");
                AudioPlayer.stopBackgroundMusic();
                AudioPlayer.playLoseGameSFX();
                reset();
                startGame();
            } else if(command.startsWith("SEND")) {
                opponentMatrix.addToGarbage(command.substring(4));
            }
        });
        
        opponentMatrix.addActionListener((ActionEvent e) -> {
            String command = e.getActionCommand();
            if(command.equals("GAMEOVER")) {
                playerScore++;
                if(playerScore == 2) {
                    playerMatrix.clearFalling();
                    opponentMatrix.clearFalling();
                    notifyListeners("MATCHOVERtrue");
                }
                System.out.println("You win! :)");
                AudioPlayer.stopBackgroundMusic();
                AudioPlayer.playWinGameSFX();
                reset();
                startGame();
            } else if(command.startsWith("SEND")) {
                playerMatrix.addToGarbage(command.substring(4));
            }
        });
    }
    
    /**
     * Resets both matrixes.
     */
    private void reset() {
        playerMatrix.reset();
        opponentMatrix.reset();
    }
    
    /**
     * Starts a game.
     */
    private void startGame() {
        playerMatrix.start();
        opponentMatrix.start();
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        drawBackground(g2D, Color.WHITE);
        
        playerMatrix.draw(g2D);
        
        g2D.translate(7.5, 0);
        int bottom = (int) (Mino.MINO_WIDTH*TetrisMatrix.VISIBLE_HEIGHT - 50);
        int startXL = -19 - 24 * FIRST_TO, startXR = -5 + 24 * FIRST_TO;
        g2D.setColor(Color.YELLOW);
        for(int i = 0; i < playerScore; i++) {
            g2D.fillRect(startXL, bottom - 37, 21, 24);
            startXL += 24;
        }
        for(int i = 0; i < opponentScore; i++) {
            g2D.fillRect(startXR, bottom - 37, 21, 24);
            startXR -= 24;
        }
        g2D.drawImage(WIN_TRACKER, null, -100, bottom - 50);
        g2D.translate(-7.5, 0);
        
        opponentMatrix.draw(g2D);
    }
    
    /**
     * Fills the background with a solid color
     * @param g2D the Graphics2D to draw with
     * @param c the color to fill with
     */
    public void drawBackground(Graphics2D g2D, Color c) {
        g2D.setColor(c);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Draws the background with an image
     * @param g2D the Graphics2D to draw with
     * @param bi the image to fill in the background with
     */
    public void drawBackground(Graphics2D g2D, BufferedImage bi) {
        g2D.drawImage(bi, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void run() {
        while(!stop) {
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.err.println("Interrupted.");
            }
        }
    }
    
    /**
     * Stops drawing.
     */
    public void stop() {
        stop = true;
    }
    
    /**
     * All ActionListeners which are listening in.
     */
    private ArrayList<ActionListener> listeners = null;
    
    /**
     * Adds a listener which is now listening in to this TetrisPanel.
     * @param al the ActionListener to add
     */
    public void addListener(ActionListener al) {
        if(listeners == null) listeners = new ArrayList<>();
        listeners.add(al);
    }
    
    /**
     * Removes all listeners which are listening in.
     */
    public void removeAllListeners() {
        listeners = null;
    }
    
    /**
     * Notifies all listeners that an event has occurred
     * @param message the message to relay
     */
    public void notifyListeners(String message) {
        if(listeners == null) return;
        ActionEvent ae = new ActionEvent(this, 0, message);
        for(ActionListener al : listeners) {
            al.actionPerformed(ae);
        }
    }
}
