package org.example;

import java.util.Random;

public class PrintPaidUser implements Runnable {
    private final RedisStorage redisStorage;
    private final int userCounter;
    private final PrintUserByOrder printUserByOrder;

    public PrintPaidUser(RedisStorage redisStorage, int userCounter, PrintUserByOrder printUserByOrder) {
        this.redisStorage = redisStorage;
        this.userCounter = userCounter;
        this.printUserByOrder = printUserByOrder;
    }

    @Override
    public void run() {
       while (true)
       {
           int interval = new Random().nextInt(userCounter/2) + 1;
           System.out.println("Interval: " + interval);
           try {
               Thread.sleep(interval*1000 - 1000);
               printUserByOrder.setShouldStop(true);
               Thread.sleep(1000);
               int user = new Random().nextInt(userCounter);
               System.out.println("Paid User " + redisStorage.getPaidUser(String.valueOf(user)));
               Thread.sleep(1000);
               printUserByOrder.setShouldStop(false);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }

       }

    }
}
