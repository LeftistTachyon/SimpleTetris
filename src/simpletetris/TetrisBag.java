package simpletetris;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class randomly generates piece order
 * @author Jed Wang
 */
public class TetrisBag {
    /**
     * The queue of Tetrominos
     */
    private Queue<Tetromino> queue;

    /**
     * Creates a new TetrisBag.
     */
    public TetrisBag() {
        queue = new LinkedList<>();
        regenerateBag();
    }
    
    /**
     * Adds 7 new tetrominos to the queue.
     */
    public void regenerateBag() {
        ArrayList<Tetromino> r = new ArrayList<>();
        r.add(new TetI());
        r.add(new TetJ());
        r.add();
    }
    
    /**
     * Returns the next tetromino in the queue.
     * Also refreshes the queue
     * @return the next tetromino
     */
    public Tetromino next() {
        Tetromino output = queue.remove();
        if(queue.size() < 7)
            regenerateBag();
    }
}
