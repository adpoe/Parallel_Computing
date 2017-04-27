package com.company;

class Processor1 {
    public void produce() throws InterruptedException {

        // acquire intrinsic lock for the class
        synchronized (this) {
            System.out.println("We are in the producer method...");
            // can only call wait inside a synchronized lock
            // hand over control
            //wait();
            wait(10000); // timeout to avoid waiting forever. this is max.
            System.out.println("Again, producer method...");
        }
    }

    public void consume() throws InterruptedException  {
        Thread.sleep(1000);
        synchronized (this) {
            System.out.println("Consumer method...");
            notify(); // tells the waiting thread that it can wake up
            // but this is non-deterministic.
            // don't know WHICH thread will wake up, if we have > 2
            Thread.sleep(3000); // Java will run this code in the block, before switching
        }
    }
}


public class WaitAndNotify {
    public static void main(String[] args) {

        final Processor1 processor = new Processor1();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
}

