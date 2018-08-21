package simpletetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;

/**
 * A MouseAdapter which listens in to key presses.
 * @author Jed Wang
 */
public class TetrisKeyAdapter extends KeyAdapter {
    /**
     * The matrix to report to.
     */
    private TetrisMatrix matrix;
    
    /**
     * Creates a new TetrisMouseAdapter.
     * @param matrix the TetrisMatrix to report to.
     */
    public TetrisKeyAdapter(TetrisMatrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case VK_LEFT:
            case VK_NUMPAD4:
                matrix.executeAction(GameAction.MOVE_LEFT);
                break;
            case VK_RIGHT:
            case VK_NUMPAD6:
                matrix.executeAction(GameAction.MOVE_RIGHT);
                break;
            case VK_SPACE:
            case VK_NUMPAD8:
                matrix.executeAction(GameAction.HARD_DROP);
                break;
            case VK_DOWN:
            case VK_NUMPAD2:
                matrix.executeAction(GameAction.MOVE_LEFT);
                break;
            case VK_UP:
            case VK_X:
            case VK_NUMPAD1:
            case VK_NUMPAD5:
            case VK_NUMPAD9:
                matrix.executeAction(GameAction.ROTATE_RIGHT);
                break;
            case VK_CONTROL:
            case VK_Z:
            case VK_NUMPAD3:
            case VK_NUMPAD7:
                matrix.executeAction(GameAction.ROTATE_LEFT);
                break;
            case VK_SHIFT:
            case VK_C:
            case VK_NUMPAD0:
                matrix.executeAction(GameAction.HOLD);
                break;
            case VK_ESCAPE:
            case VK_F1:
            case VK_P:
                matrix.executeAction(GameAction.PAUSE);
                break;
        }
    }
    
    /**
     * All possible game actions
     */
    public static enum GameAction {
        MOVE_LEFT, MOVE_RIGHT, HARD_DROP, SOFT_DROP, 
                ROTATE_RIGHT, ROTATE_LEFT, HOLD, PAUSE;
    }
}