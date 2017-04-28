package com.company;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *   Setup:
 *   5 philosophers
 *   5 chopsticks
 *
 *   Actions:  Eat/Think
 *   Need BOTH left and right chopsticks to eat
 *   A chopstick can only be held by 1 philosopher at any time
 *
 *   Problem:  Write a concurrent algorithm such that no philsopher will starve.
 */
public class DiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = null;
        Philosopher[] philosophers = null;

        try {
            philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSPHERS];
            Chopstick[] chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

            for (int i=0; i<Constants.NUMBER_OF_CHOPSTICKS; i++) {
                chopsticks[i] = new Chopstick(i); // id is index
            }

            // each philosopher gets own thread & state
            executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSPHERS);

            for (int i=0; i<Constants.NUMBER_OF_PHILOSPHERS; i++) {
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i+1) % Constants.NUMBER_OF_CHOPSTICKS]);
                executorService.execute(philosophers[i]);
            }

            Thread.sleep(Constants.SIMULATION_RUNNING_TIME); // throws interrupted exception

            for (Philosopher p : philosophers) {
                p.setFull(true);
            }

        } finally {
            // complete previous tasks, but don't accept new ones
            executorService.shutdown();

            // and then wait to ensure it terminates
            while(!executorService.isTerminated()) {
                Thread.sleep(1000);
            }

            // ensure philosphers are done
            for (Philosopher p : philosophers) {
                System.out.println(p + " eats " + p.getCounter());
            }
        }
    }
}

class Constants {

    // holds constants. make constructor private
    // to avoid being instantiated
    private Constants() {
    }

    public static final int NUMBER_OF_PHILOSPHERS = 5;
    public static final int NUMBER_OF_CHOPSTICKS = 5;
    public static final int SIMULATION_RUNNING_TIME = 5*1000; // in milliseconds
}

class Chopstick {
    private int id;
    private Lock lock;

    public Chopstick(int id) {
        this.id = id;
        // Re-entrant lock lets us lock in one method & unlock in another
        this.lock = new ReentrantLock();
    }

    public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
        // try to acquire lock on this chopstick for 10ms
        if (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println(philosopher + "picked up " + state.toString() + " " + this);
            return true; // if successful
        }

        return false; // if can't pickup chopstick
    }


    public void putDown(Philosopher philosopher, State state) {
        // unlock this chopstick
        lock.unlock();
        System.out.println(philosopher + " put down " + this);
    }

    @Override
    public String toString() {
        return "Chopstick " + id;
    }

}

class Philosopher implements Runnable {
    private int id;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    // eat & think for random amounts of time
    private Random random;
    private int eatingCounter;
    private volatile boolean isFull = false; // VOLATILE ensures we get var from main memory.
                                             // NOT cache.
                                             // Each philosopher is a thread.
                                             // and each thread has its own cache.

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            // while not full
            while (!isFull) {
                think();
                if (leftChopstick.pickUp(this, State.LEFT)) {
                    if(rightChopstick.pickUp(this, State.RIGHT)) {
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);
                    }
                    leftChopstick.putDown(this, State.LEFT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void think() throws InterruptedException {
        System.out.println(this + " is thinking...");
        Thread.sleep(random.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        System.out.println(this + " is thinking...");
        eatingCounter++;
        Thread.sleep(random.nextInt(1000));
    }
    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public int getCounter() {
        return this.eatingCounter;
    }

    @Override
    public String toString() {
        return " Philosopher " + id;
    }
}

enum State {
    LEFT, RIGHT
}
