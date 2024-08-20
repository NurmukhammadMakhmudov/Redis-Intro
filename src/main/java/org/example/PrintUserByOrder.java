package org.example;

public class PrintUserByOrder implements Runnable {

    private final RedisStorage redisStorage;
    private volatile boolean shouldStop = false;

    public PrintUserByOrder(RedisStorage redisStorage) {
        this.redisStorage = redisStorage;
    }

    @Override
    public void run() {
       while (true) {
          if (!shouldStop) {
              System.out.println("Users by order " + redisStorage.getNextUser());
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
          }
       }
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }
}
