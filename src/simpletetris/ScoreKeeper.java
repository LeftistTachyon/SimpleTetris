package simpletetris;

/**
 * Keeps track of the score
 * @author Jed Wang
 */
public class ScoreKeeper {
    /**
     * The lines sent
     */
    private int lines;
    
    /**
     * Keeps track of combos
     */
    private int combo;
    
    /**
     * Whether the player is doing powerful moves back to back
     */
    private boolean b2b;
    
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
        lines = 0;
        
        combo = -1;
        b2b = false;
    }
    
    /**
     * Notifies this ScoreKeeper that new Lines have been cleared.
     * @param linesCleared how many new lines have been cleared
     * @param clearType what type of line clear (e.g. standard, t-spin, 
     * t-spin mini)
     */
    public void newLinesCleared(int linesCleared, int clearType) {
        if(linesCleared == 0) {
            b2b = false;
            combo = -1;
        }
        
        if(linesCleared > 4 || linesCleared < 0) 
            throw new IllegalArgumentException("You cleared more than "
                    + "4 lines at once or less than 0 lines.");
        switch(clearType) {
            case NORMAL:
                switch(linesCleared) {
                    case 1:
                        // single
                        // 0 extra
                        b2b = false;
                        break;
                    case 2:
                        // double
                        lines += 1;
                        b2b = false;
                        break;
                    case 3:
                        // triple
                        lines += 2;
                        b2b = false;
                        break;
                    case 4:
                        // tetris
                        lines += 4;
                        b2b = true;
                        break;
                }
                break;
            case T_SPIN:
                switch(linesCleared) {
                    case 1:
                        // T-spin single
                        lines += 2;
                        break;
                    case 2:
                        // T-spin double
                        lines += 4;
                        break;
                    case 3:
                        // T-spin triple
                        lines += 6;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "You cleared 4 lines with a tetris?");
                }
                b2b = true;
                break;
            case T_SPIN_MINI:
                // T-spin mini
                // 0 extra
                b2b = true;
                break;
        }
        
        combo++;
        lines += comboBonus();
        if(b2b) lines++;
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
        return lines;
    }
}