package com.company;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StudentsLibrary {
    public static void main(String[] args) {
        Student[] students = null;
        Book[] books = null;
        ExecutorService executorService = Executors.newFixedThreadPool(Constants1.NUMBER_OF_STUDENTS);

        try {
            books = new Book[Constants1.NUMBER_OF_BOOKS];
            students = new Student[Constants.NUMBER_OF_CHOPSTICKS];

            for (int i=0; i<Constants1.NUMBER_OF_BOOKS;i++) {
                books[i] = new Book(i);
            }

            for (int i=0; i<Constants1.NUMBER_OF_STUDENTS; i++) {
                students[i] = new Student(i, books);
                executorService.execute(students[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}

class Book {
    private int id;
    private Lock lock;

    public Book(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public void read(Student student) throws InterruptedException {
        lock.tryLock(1, TimeUnit.MINUTES);
        System.out.println(student + " starts reading " + this);
        Thread.sleep(2000);
        lock.unlock();
        System.out.println(student + " has finished reading " + this);
    }

    @Override
    public String toString() {
        return " Book #" + id;
    }
}

class Student implements Runnable {

    private int id;
    private Book[] books;

    public Student(int id, Book[] books) {
        this.id = id;
        this.books = books;
    }

    @Override
    public void run() {
        Random random = new Random();

        while(true) {
            int bookId = random.nextInt(Constants1.NUMBER_OF_BOOKS);

            try {
                books[bookId].read(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Student #" + id;
    }
}

class Constants1 {
    private Constants1() {
    }

    public static final int NUMBER_OF_STUDENTS = 5;
    public static final int NUMBER_OF_BOOKS = 7;
}