package simpletetris;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A class that handles dealing garbage to the Matrix<br>
 * Handles incoming garbage
 * @author Jed Wang
 */
public class GarbageDealer {
    /**
     * The queue for garbage
     */
    private Deque<Integer> garbageQueue;
    
    /**
     * Creates a new GarbageDealer
     */
    public GarbageDealer() {
        garbageQueue = new LinkedList<>();
    }
    
    /**
     * Adds garbage to the queue
     * @param lines a String representation of the lines sent
     */
    public void addGarbage(String lines) {
        Scanner adder = new Scanner(lines);
        while(adder.hasNextInt()) garbageQueue.add(adder.nextInt());
    }
    
    /**
     * Counters the incoming garbage with this garbage
     * @param counterLines a String representation of the lines to send
     * @return a String representation of the lines to send to the opponent<br>
     * Returns null if no garbage is sent to the opponent
     */
    public String counterGarbage(String counterLines) {
        if(garbageQueue.isEmpty()) {
            return counterLines;
        }
        
        Scanner adder = new Scanner(counterLines);
        while(adder.hasNextInt()) {
            int counter = -adder.nextInt();
            System.out.println(counter);
            while(counter < 0 && !garbageQueue.isEmpty()) {
                counter += garbageQueue.removeFirst();
            }
            if(counter > 0) garbageQueue.addFirst(counter);
        }
        
        if(adder.hasNextInt()) {
            String output = "";
            while(adder.hasNextInt()) {
                output += adder.nextInt() + " ";
            }
            System.out.println(output.length());
            return output.trim();
        } else return null;
    }
    
    /**
     * Returns the next chunk of garbage to add to the bottom and removes it
     * @return the next chunk of garbage to add to the bottom<br>
     * Returns 0 if the queue is empty
     */
    public int getNextGarbage() {
        Integer next = garbageQueue.pollFirst();
        return (next == null)?0:next;
    }
    
    /**
     * Returns the next chunk of garbage to add to the bottom
     * @return the next chunk of garbage to add to the bottom<br>
     * Returns 0 if the queue is empty
     */
    public int peekNextGarbage() {
        Integer next = garbageQueue.peekFirst();
        return (next == null)?0:next;
    }
}
