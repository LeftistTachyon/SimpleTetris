package simpletetris;

import java.awt.Dimension;
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
        super.setSize(new Dimension(TetrisMatrix.WIDTH*Mino.MINO_WIDTH + 45, 
                (int) (TetrisMatrix.VISIBLE_HEIGHT*Mino.MINO_WIDTH) + 30));
        super.setResizable(true);
        super.getContentPane().add(panel);
        
        TetrisKeyAdapter tka = new TetrisKeyAdapter(panel.matrix);
        super.addKeyListener(tka);
        
        super.setVisible(true);
        new Thread(panel).start();
    }
}