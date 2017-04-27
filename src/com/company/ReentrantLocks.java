package com.company;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLocks {
    // lets us avoid starvation, with a 'fairness' option
    // so that LONGEST waiting thread can get the lock
    // avoids infinite waiting
    private static int counter = 0;
    private static Lock lock = new ReentrantLock();

    public static void increment() {


        // between locks is the critical section
        lock.lock();
        // leads to inconsistent results unless we lock first
        // threads will double-increment sometimes, and not others
        try {
            for (int i=0; i<10000;i++)
                counter++;
            // use try/finally to ensure we unlock, if there is an exception/crash
            // otherwise, we'll never give up the lock
            // and there will be deadlock
        } finally {
            lock.unlock(); // now, it's consistent. will ALWAYS unlock.
        }
    }


    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                increment();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                increment();
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

        System.out.println(("Counter is ")+counter);
    }
}
