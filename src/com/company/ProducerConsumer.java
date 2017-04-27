package com.company;


import java.util.ArrayList;
import java.util.List;

class Processor {

    private List<Integer> list = new ArrayList<Integer>();
    private final int LIMIT = 5;
    private final int BOTTOM = 0;
    private final Object lock = new Object();
    private int value = 0;

    public void producer() throws InterruptedException {

        synchronized (lock) {
            while (true) {
                if( list.size() == LIMIT) {
                    System.out.println("Waiting for removing items from the list...");
                    lock.wait(); // if sync on THIS --> calls for the class
                } else {
                    // else not at limit, so we can add items to the list
                    System.out.println("Adding "+ value);
                    list.add(value);
                    value++;
                    lock.notify(); // after notifying, we don't hand over execution to other thread
                                   // until we hit a wait
                }
                Thread.sleep(1000);
            }
        }

    }

    public void consumer() throws InterruptedException  {

        synchronized (lock) {
            while (true) {
               if (list.size() == BOTTOM ) { // if list.size == BOTTOM --> list is empty
                   System.out.println("Waiting to add items to the list...");
                   lock.wait();
               } else {
                   // list isn't empty, remove items
                   System.out.println("Removed: "+list.remove(list.size()-1));
                   lock.notify();
               }
               Thread.sleep(1000);
            }
        }

    }
}


public class ProducerConsumer {
    public static void main(String[] args) {

        final Processor processor = new Processor();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.consumer();
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