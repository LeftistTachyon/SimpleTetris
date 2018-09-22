package simpletetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
     */
    public void newLinesCleared(int linesCleared, int clearType, 
            boolean perfectClear) {
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
                        System.out.println("lmao1");
                        bb = false;
                        break;
                    case 2:
                        // double
                        newLinesToSend = 1;
                        System.out.println("lmao2");
                        bb = false;
                        break;
                    case 3:
                        // triple
                        newLinesToSend = 2;
                        System.out.println("lmao3");
                        bb = false;
                        break;
                    case 4:
                        // tetris
                        newLinesToSend = 4;
                        System.out.println("lmao4");
                        bb = true;
                        break;
                }
                break;
            case T_SPIN:
                switch(linesCleared) {
                    case 1:
                        // T-spin single
                        newLinesToSend = 2;
                        System.out.println("lmaoT1");
                        break;
                    case 2:
                        // T-spin double
                        newLinesToSend = 4;
                        System.out.println("lmaoT2");
                        break;
                    case 3:
                        // T-spin triple
                        newLinesToSend = 6;
                        System.out.println("lmaoT3");
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "You cleared 4 lines with a t-piece?");
                }
                System.out.println("lmaoT");
                bb = true;
                break;
            case T_SPIN_MINI:
                // T-spin mini
                newLinesToSend = 1;
                System.out.println("lmaot");
                bb = true;
                break;
        }
        
        
        combo++;
        newLinesToSend += comboBonus();
        if(perfectClear) newLinesToSend += 10;
        if(b2b && bb) newLinesToSend++;
        
        if(linesCleared == 4) System.out.println("BB: " + bb);
        System.out.println("B2B: " + b2b);
        
        b2b = bb;
        linesToSend += newLinesToSend;
        
        if(newLinesToSend != 0) linesToSendCommand += newLinesToSend + " ";
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
}