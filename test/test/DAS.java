package test;

public class DAS {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("a");
        Thread.sleep(250);
        while(true) {
            System.out.println("a");
            Thread.sleep(50);
        }
    }
}