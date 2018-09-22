package test;

import simpletetris.GarbageDealer;

public class GarbageCancelling {
    public static void main(String[] args) {
        GarbageDealer gd = new GarbageDealer();
        gd.addGarbage("1 2 3 4 5");
        
        System.out.println(gd.toString());
        
        gd.counterGarbage("2 1 2 3 4 5");
        
        System.out.println(gd.toString());
    }
}