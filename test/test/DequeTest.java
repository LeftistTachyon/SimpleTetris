package test;

import java.util.Deque;
import java.util.LinkedList;

public class DequeTest {
    public static void main(String[] args) {
        System.out.println(test()); // NullPointerException
    }
    
    public static int test() {
        Deque<Integer> deque = new LinkedList<>();
        return deque.pollFirst(); // null
    }
}