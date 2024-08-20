package org.example;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public class RedisStorage {

private RedissonClient redissonClient;
private  RKeys keys;
private String KEY = "USERS_QUEUE";
private final RMap<Integer, String> map;


    private final int userCount;
private int userCounter = 0;

    public RedisStorage(int userCount) {
        this.userCount = userCount;

        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        try{
            redissonClient = Redisson.create(config);
        }catch (RedisConnectionException e)
        {
            System.out.println("Redis connection failed");
            e.printStackTrace();
        }
        map = redissonClient.getMap(KEY);
       for (int i = 0; i < userCount; i++) {
           map.put(i, String.valueOf(i));
       }

    }

    public String getNextUser() {

        synchronized (map) {
            return map.get(userCounter++ % userCount);
        }

    }
    public String getPaidUser(String userName)
    {
       try{
           synchronized (map) {
               return map.entrySet().stream().filter(k-> k.getValue().equals(userName))
                       .findFirst()
                       .orElseThrow( ()-> new UserPrincipalNotFoundException("User Not Found" + userName)).getValue();
           }
       }
       catch (UserPrincipalNotFoundException e)
       {
           e.printStackTrace();
           return null;
       }
    }
    public int getUserCount() {
        return userCount;
    }

    public void shutdown()
    {
        redissonClient.shutdown();
    }




}
