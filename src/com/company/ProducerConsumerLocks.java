package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Worker1 {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void producer() throws InterruptedException {
        lock.lock();
        System.out.println("Producer method...");
        condition.await(); // wait hands over to other threads waiting on same lock
        System.out.println("Producer again...");
        lock.unlock();
    }

    public void consumer() throws InterruptedException {
        lock.lock(); // runs after await
        Thread.sleep(2000);
        System.out.println("Consumer method...");
        condition.signal(); // signal is opposite of await
        lock.unlock(); // then unlocks

    }

}


public class ProducerConsumerLocks {

    public static void main(String[] args) {
        final Worker1 worker1 = new Worker1();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker1.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker1.consumer();
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
