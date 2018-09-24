package simpletetris;

/**
 * The main class for this application.
 * @author Jed Wang, Grace Liu, Danny Tang
 */
public class SimpleTetrisMain {
    /**
     * The main method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TetrisFrame();
        AudioPlayer.playInGameBackground();
    }
}
