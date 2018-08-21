package simpletetris;

import javax.swing.JFrame;

/**
 * The Frame for this application
 * @author Jed Wang
 */
public class TetrisFrame extends JFrame {
    /**
     * The Panel for this application
     */
    private TetrisPanel panel;

    /**
     * Creates a new TetrisFrame.
     */
    public TetrisFrame() {
        super("Simple Tetris");
        panel = new TetrisPanel();
        
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(true);
        super.getContentPane().add(panel);
        
        super.setVisible(true);
        new Thread(panel).start();
    }
}