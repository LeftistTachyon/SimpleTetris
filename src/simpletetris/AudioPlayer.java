package simpletetris;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.SwingUtilities;

/**
 * A class that plays audio on command
 * @author Jed Wang
 */
public class AudioPlayer {
    /**
     * Standard music during gameplay.<br>
     * Taken from Super Mario World 2: Yoshi's Island.
     * Composed by Koji Kondo
     */
    private static final Media FLOWER_GARDEN;
    
    /**
     * Standard music during gameplay.<br>
     * Taken from Super Mario World 2: Yoshi's Island.
     * Composed by Koji Kondo
     */
    private static final Media ATHLETIC;
    
    /**
     * Standard music during gameplay.<br>
     * Taken from Super Mario World 2: Yoshi's Island.
     * Composed by Koji Kondo
     */
    private static final Media OVERWORLD;
    
    /**
     * The MediaPlayer which controls playing background music.
     */
    private static MediaPlayer backgroundPlayer;
    
    /**
     * Stores whether JavaFX has been initialized.
     */
    private static boolean initialized = false;
    
    static {
        FLOWER_GARDEN = new Media(
                new File("music/flowergarden.m4a").toURI().toString());
        
        ATHLETIC = new Media(
                new File("music/athletic.m4a").toURI().toString());
        
        OVERWORLD = new Media(
                new File("music/overworld.m4a").toURI().toString());
    }
    
    /**
     * Just execute static methods.
     */
    private AudioPlayer() {}
    
    /**
     * Plays a random background music allocated for in-game.
     */
    public static void playInGameBackground() {
        if(!initialized)  
            initialize();
        
        if(backgroundPlayer != null) 
            backgroundPlayer.stop();
        
        int r = (int) (Math.random() * 3);
        switch(r) {
            case 0:
                backgroundPlayer = new MediaPlayer(ATHLETIC);
                break;
            case 1:
                backgroundPlayer = new MediaPlayer(FLOWER_GARDEN);
                break;
            case 2:
                backgroundPlayer = new MediaPlayer(OVERWORLD);
                break;
        }
        backgroundPlayer.play();
    }
    
    /**
     * Initializes JavaFX.
     */
    private static void initialize() {
        try {
            SwingUtilities.invokeAndWait(JFXPanel::new);
        } catch (InterruptedException ex) {
            System.err.println("Initialization interrupted: " + ex.toString());
        } catch (InvocationTargetException ex) {
            System.err.println("Unable to create a new JFXPanel");
        }
        
        initialized = true;
    }
}