package simpletetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import static simpletetris.TetrisMatrix.*;

/**
 * Keeps track of the score<br>
 * Handles outgoing garbage
 * @author Jed Wang
 */
public class ScoreKeeper {
    /**
     * The lines sent
     */
    private int linesSent;
    
    /**
     * The number of lines to send
     */
    private int linesToSend;
    
    /**
     * The command to send lines
     */
    private String linesToSendCommand;
    
    /**
     * Keeps track of combos
     */
    private int combo;
    
    /**
     * Whether the player is doing powerful moves back to back
     */
    private boolean b2b;
    
    /**
     * A collection of listeners which are listening to this ScoreKeeper.
     */
    private ArrayList<ActionListener> listeners = null;
    
    /**
     * A normal lines clear
     */
    public static final int NORMAL = 0;
    
    /**
     * A standard t-spin
     */
    public static final int T_SPIN = 1;
    
    /**
     * A t-spin mini
     */
    public static final int T_SPIN_MINI = 2;

    /**
     * Creates a brand new ScoreKeeper.
     */
    public ScoreKeeper() {
        linesSent = 0;
        linesToSend = 0;
        linesToSendCommand = "";
        
        combo = 0;
        b2b = false;
    }
    
    /**
     * Notifies this ScoreKeeper that new Lines have been cleared.
     * @param linesCleared how many new lines have been cleared
     * @param clearType what type of line clear (e.g. standard, t-spin, 
     * t-spin mini)
     * @param perfectClear whether the move resulted in a perfect clear
     * @param hasGarbage whether there is garbage queued up
     */
    public void newLinesCleared(int linesCleared, int clearType, 
            boolean perfectClear, boolean hasGarbage) {
        if(linesCleared == 0) {
            combo = 0;
            
            String command = linesToSendCommand.trim();
            if(command.length() > 0)
                notifyListeners(linesToSendCommand.trim());
            linesSent += linesToSend;
            linesToSend = 0;
            linesToSendCommand = "";
            
            return;
        }
        
        if(linesCleared > 4 || linesCleared < 0) 
            throw new IllegalArgumentException("You cleared more than "
                    + "4 lines at once or less than 0 lines.");
        boolean bb = false;
        int newLinesToSend = 0;
        switch(clearType) {
            case NORMAL:
                switch(linesCleared) {
                    case 1:
                        // single
                        // 0 extra
                        bb = false;
                        break;
                    case 2:
                        // double
                        newLinesToSend = 1;
                        bb = false;
                        break;
                    case 3:
                        // triple
                        newLinesToSend = 2;
                        bb = false;
                        break;
                    case 4:
                        // tetris
                        newLinesToSend = 4;
                        bb = true;
                        break;
                }
                break;
            case T_SPIN:
                switch(linesCleared) {
                    case 1:
                        // T-spin single
                        newLinesToSend = 2;
                        break;
                    case 2:
                        // T-spin double
                        newLinesToSend = 4;
                        break;
                    case 3:
                        // T-spin triple
                        newLinesToSend = 6;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "You cleared 4 lines with a t-piece?");
                }
                bb = true;
                break;
            case T_SPIN_MINI:
                // T-spin mini
                newLinesToSend = 1;
                bb = true;
                break;
        }
        
        
        combo++;
        newLinesToSend += comboBonus();
        if(perfectClear) newLinesToSend += 10;
        if(b2b && bb) newLinesToSend++;
        
        b2b = bb;
        linesToSend += newLinesToSend;
        
        if(newLinesToSend != 0) linesToSendCommand += newLinesToSend + " ";
        
        if(hasGarbage) {
            String command = linesToSendCommand.trim();
            if(command.length() > 0)
                notifyListeners(linesToSendCommand.trim());
            linesSent += linesToSend;
            linesToSend = 0;
            linesToSendCommand = "";
        }
    }
    
    /**
     * Determines the bonus for the combo
     * @return the combo bonus
     */
    private int comboBonus() {
        if(combo > 10) {
            return 5;
        } else if(combo > 7) {
            return 4;
        } else if(combo > 5) {
            return 3;
        } else if(combo > 3) {
            return 2;
        } else if(combo > 1) {
            return 1;
        } else return 0;
    }

    /**
     * Returns the number of lines sent
     * @return the number of lines sent
     */
    public int getLinesSent() {
        return linesSent;
    }
    
    /**
     * Adds an <code>ActionListener</code> to this ScoreKeeper
     * @param listener an ActionListener to add
     */
    public void addListener(ActionListener listener) {
        if(listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }
    
    /**
     * Removes all listeners.
     */
    public void removeAllListeners() {
        listeners = new ArrayList<>();
    }
    
    /**
     * Notifies all listeners about an event.
     * @param message the message to convey
     */
    private void notifyListeners(String message) {
        if(listeners == null) return;
        ActionEvent event = new ActionEvent(this, 0, message);
        for(ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }
    
    /**
     * Determines how a bar should be filled
     * @return how a bar should be filled
     */
    public int[] getBarFill() {
        if(linesToSend == 0) return null;
        
        int[] output = new int[20];
        for(int i = 0; i < output.length; i++) {
            output[i] = linesToSend/20;
        }
        
        int leftovers = linesToSend%20;
        for(int i = output.length-1; i >= output.length - leftovers; i--) {
            output[i]++;
        }
        
        return output;
    }
}