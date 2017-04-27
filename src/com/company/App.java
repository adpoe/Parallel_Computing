package com.company;

class Runner1 extends Thread {
    @Override
    public void run() {
        for (int i=0; i<99; ++i) {
            System.out.println("Runner1: " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Runner2 extends Thread {
    @Override
    public void run() {
        for (int i=0; i<99; ++i) {
            System.out.println("Runner2: " + i);
        }
    }
}
public class App {
    public static void main(String[] args) {
        //Thread t1 = new Thread(new Runner1());
        //Thread t2 = new Thread(new Runner2());

        Runner1 t1 = new Runner1();
        Runner2 t2 = new Runner2();

        t1.start();
        t2.start();

        // join means wait until these threads finish until
        // we move to next command in main thread of execution (application thread)
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // application thread runs first, unless we run a join first
        System.out.println("Finished the tasks...");
    }
}
