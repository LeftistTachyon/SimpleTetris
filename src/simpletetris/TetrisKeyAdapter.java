package simpletetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.util.HashMap;

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
     * Stores whether a key is pressed
     */
    private volatile HashMap<GameAction, Boolean> pressed;
    
    /**
     * Creates a new TetrisMouseAdapter.
     * @param matrix the TetrisMatrix to report to.
     */
    public TetrisKeyAdapter(TetrisMatrix matrix) {
        this.matrix = matrix;
        
        pressed = new HashMap<>();
        for(GameAction value : GameAction.values()) {
            pressed.put(value, false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case VK_LEFT:
            case VK_NUMPAD4:
                if(pressed.get(GameAction.MOVE_LEFT))
                    break;
                pressed.put(GameAction.MOVE_LEFT, true);
                DASaction(GameAction.MOVE_LEFT);
                break;
            case VK_RIGHT:
            case VK_NUMPAD6:
                if(pressed.get(GameAction.MOVE_RIGHT))
                    break;
                pressed.put(GameAction.MOVE_RIGHT, true);
                DASaction(GameAction.MOVE_RIGHT);
                break;
            case VK_SPACE:
            case VK_NUMPAD8:
                if(pressed.get(GameAction.HARD_DROP))
                    break;
                pressed.put(GameAction.HARD_DROP, true);
                matrix.executeAction(GameAction.HARD_DROP);
                break;
            case VK_DOWN:
            case VK_NUMPAD2:
                if(pressed.get(GameAction.SOFT_DROP))
                    break;
                pressed.put(GameAction.SOFT_DROP, true);
                DASaction(GameAction.SOFT_DROP);
                break;
            case VK_UP:
            case VK_X:
            case VK_NUMPAD1:
            case VK_NUMPAD5:
            case VK_NUMPAD9:
                if(pressed.get(GameAction.ROTATE_RIGHT))
                    break;
                pressed.put(GameAction.ROTATE_RIGHT, true);
                matrix.executeAction(GameAction.ROTATE_RIGHT);
                break;
            case VK_CONTROL:
            case VK_Z:
            case VK_NUMPAD3:
            case VK_NUMPAD7:
                if(pressed.get(GameAction.ROTATE_LEFT))
                    break;
                pressed.put(GameAction.ROTATE_LEFT, true);
                matrix.executeAction(GameAction.ROTATE_LEFT);
                break;
            case VK_SHIFT:
            case VK_C:
            case VK_NUMPAD0:
                if(pressed.get(GameAction.HOLD))
                    break;
                pressed.put(GameAction.HOLD, true);
                matrix.executeAction(GameAction.HOLD);
                break;
            /*case VK_ESCAPE:
            case VK_F1:
            case VK_P:
                if(pressed.get(GameAction.PAUSE))
                    break;
                pressed.put(GameAction.PAUSE, true);
                matrix.executeAction(GameAction.PAUSE);
                break;*/
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case VK_LEFT:
            case VK_NUMPAD4:
                pressed.put(GameAction.MOVE_LEFT, false);
                break;
            case VK_RIGHT:
            case VK_NUMPAD6:
                pressed.put(GameAction.MOVE_RIGHT, false);
                break;
            case VK_SPACE:
            case VK_NUMPAD8:
                pressed.put(GameAction.HARD_DROP, false);
            case VK_DOWN:
            case VK_NUMPAD2:
                pressed.put(GameAction.SOFT_DROP, false);
                break;
            case VK_UP:
            case VK_X:
            case VK_NUMPAD1:
            case VK_NUMPAD5:
            case VK_NUMPAD9:
                pressed.put(GameAction.ROTATE_RIGHT, false);
                break;
            case VK_CONTROL:
            case VK_Z:
            case VK_NUMPAD3:
            case VK_NUMPAD7:
                pressed.put(GameAction.ROTATE_LEFT, false);
                break;
            case VK_SHIFT:
            case VK_C:
            case VK_NUMPAD0:
                pressed.put(GameAction.HOLD, false);
                break;
            /*case VK_ESCAPE:
            case VK_F1:
            case VK_P:
                pressed.put(GameAction.PAUSE, false);
                break;*/
        }
    }
    
    /**
     * DAS-es an action
     * @param action the action to DAS
     */
    private void DASaction(GameAction action) {
        new Thread(new DASer(action)).start();
    }
    
    /**
     * counter
    */
    private static int cnt = 0;
    
    /**
     * A class that handles DAS-ing: the delay as well as the repetition.
     * @see http://tetris.wikia.com/wiki/DAS
     */
    private class DASer implements Runnable {
        /**
         * The action to execute
         */
        private GameAction toExecute;

        /**
         * Creates a DASer and gives it an action to DAS
         * @param toExecute the action to DAS
         */
        public DASer(GameAction toExecute) {
            this.toExecute = toExecute;
        }
        
        @Override
        public void run() {
            cnt++;
            matrix.executeAction(toExecute);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                System.err.println("DAS thread interrupted.");
            }
            
            while(pressed.get(toExecute)) {
                if(cnt > 1) {
                    cnt--;
                    return;
                }
                matrix.executeAction(toExecute);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    System.err.println("DAS thread interrupted.");
                }
            }
            cnt--;
        }
    }
    
    /**
     * All possible game actions
     */
    public static enum GameAction {
        MOVE_LEFT, MOVE_RIGHT, HARD_DROP, SOFT_DROP, 
                ROTATE_RIGHT, ROTATE_LEFT, HOLD, /*PAUSE,*/ GRAVITY;
    }
}
