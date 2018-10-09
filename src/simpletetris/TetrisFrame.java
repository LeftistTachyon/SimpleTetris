package simpletetris;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Frame for this application
 * @author Jed Wang
 */
public class TetrisFrame extends JFrame {
    /**
     * The Panel for this application
     */
    public final TetrisPanel panel;

    /**
     * Creates a new TetrisFrame.
     */
    public TetrisFrame() {
        super("Simple Tetris");
        panel = new TetrisPanel();
        
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(new Dimension(2*TetrisMatrix.WIDTH*Mino.MINO_WIDTH + 530, 
                (int) ((TetrisMatrix.VISIBLE_HEIGHT+1)*Mino.MINO_WIDTH)+15));
        super.setResizable(true);
        super.getContentPane().add(panel);
        
        TetrisKeyAdapter tka = new TetrisKeyAdapter(panel.playerMatrix);
        super.addKeyListener(tka);
        
        TetrisFrame _this = this;
        
        panel.addListener((ActionEvent e) -> {
            String message = e.getActionCommand();
            if(message.startsWith("MATCHOVER")) {
                if(Boolean.parseBoolean(message.substring(9))) {
                    // I won!
                    AudioPlayer.playWinMatchSFX();
                    JOptionPane.showMessageDialog(_this, "You Won!",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    // I lost....
                    AudioPlayer.playLoseMatchSFX();
                    JOptionPane.showMessageDialog(_this, "You lost.",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        });
        
        super.setVisible(true);
        new Thread(panel).start();
    }
}