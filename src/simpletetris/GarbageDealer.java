package simpletetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import static simpletetris.TetrisMatrix.*;

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
     * The number of lines pending in the queue
     */
    private int linesToRecieve;
    
    /**
     * Creates a new GarbageDealer
     */
    public GarbageDealer() {
        garbageQueue = new LinkedList<>();
        linesToRecieve = 0;
    }
    
    /**
     * Adds garbage to the queue
     * @param lines a String representation of the lines sent
     */
    public void addGarbage(String lines) {
        Scanner adder = new Scanner(lines);
        while(adder.hasNextInt()) {
            int next = adder.nextInt();
            garbageQueue.add(next);
            linesToRecieve += next;
        }
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
            linesToRecieve += counter;
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
        int output = (next == null)?0:next;
        linesToRecieve -= output;
        return output;
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
    
    /**
     * Returns whether there is garbage queued up
     * @return whether there is garbage queued up
     */
    public boolean hasGarbage() {
        return !garbageQueue.isEmpty();
    }

    @Override
    public String toString() {
        return garbageQueue.toString();
    }
    
    /**
     * Determines how a bar should be filled
     * @return how a bar should be filled
     */
    public int[] getBarFill() {
        int[] output = new int[20];
        for(int i = 0; i < output.length; i++) {
            output[i] = linesToRecieve/20;
        }
        
        int leftovers = linesToRecieve%20;
        for(int i = output.length-1; i >= leftovers; i--) {
            output[i]++;
        }
        
        return output;
    }
}
