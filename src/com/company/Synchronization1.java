package com.company;

/**
 * Created by tony on 4/27/17.
 */
public class Synchronization1 {

    private static int counter = 0;

    // slower, but more consistent --> ensures no threading errors
    // but incurs threading overhead
    public static synchronized void increment() {
        ++counter;
    }

    public static void process() {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i=0; i<100; ++i) {
                    increment();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i=0; i<100; ++i) {
                    increment();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        process();
        // threads sometimes touch same value here
        System.out.println(counter);
    }
}
