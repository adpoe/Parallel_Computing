package com.company;

class Worker implements Runnable {
    private volatile boolean isTerminated = false; // means always read from main memory

    @Override
    public void run() {
        while(!isTerminated()) {
            System.out.println("Hello from worker class...");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean terminated) {
        isTerminated = terminated;
    }
}


public class VolatileExample {
    public static void  main(String[] args) {
        Worker worker = new Worker();

        Thread t1 = new Thread(worker);
        t1.start();


        try {
            Thread.sleep(3000); // sleep main thread for 3 secs
                                // while worker keeps printing every 300ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.setTerminated(true);
        System.out.println("Finished...");
    }
}
