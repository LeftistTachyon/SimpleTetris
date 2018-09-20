package simpletetris;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private TetrisPanel panel;

    /**
     * Creates a new TetrisFrame.
     */
    public TetrisFrame() {
        super("Simple Tetris");
        panel = new TetrisPanel();
        
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(new Dimension(TetrisMatrix.WIDTH*Mino.MINO_WIDTH + 265, 
                (int) ((TetrisMatrix.VISIBLE_HEIGHT+1)*Mino.MINO_WIDTH)+15));
        super.setResizable(true);
        super.getContentPane().add(panel);
        
        TetrisKeyAdapter tka = new TetrisKeyAdapter(panel.matrix);
        super.addKeyListener(tka);
        
        TetrisFrame _this = this;
        
        panel.matrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = e.getActionCommand();
                // one-string commands
                switch(message) {
                    case "GAMEOVER":
                        JOptionPane.showMessageDialog(_this, "Game Over!", 
                                "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                        break;
                }
                
                // info commands
            }
        });
        
        super.setVisible(true);
        new Thread(panel).start();
    }
}