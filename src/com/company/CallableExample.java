package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


class Processor2 implements Callable<String> {
    private int id;

    public Processor2(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return "Id " + id;
    }
}
public class CallableExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Future<String>> list = new ArrayList<Future<String>>();

        for (int i=0; i<5; ++i) {
            Future<String> future = executorService.submit(new Processor2(i+1));
            list.add(future);
        }

        for(Future<String> future : list) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}
