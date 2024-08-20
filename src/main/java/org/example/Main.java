package org.example;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        RedisStorage redisStorage = new RedisStorage(20);
        PrintUserByOrder printUserByOrder = new PrintUserByOrder(redisStorage);
        PrintPaidUser printPaidUser = new PrintPaidUser(redisStorage, redisStorage.getUserCount(),printUserByOrder);
    Thread paidUsers = new Thread(printPaidUser);
    Thread usersByOrder = new Thread(printUserByOrder);

            paidUsers.start();
            usersByOrder.start();

        System.out.println("Hello World!");
        usersByOrder.join();
        paidUsers.join();
        redisStorage.shutdown();

    }
}