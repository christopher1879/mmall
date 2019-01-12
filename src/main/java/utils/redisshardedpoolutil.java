package utils;

import commons.redisshardedpool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class redisshardedpoolutil {





    public  static  Long setnx(String key,String value)
    {
        ShardedJedis jedis =null;
        Long result = null;
        try {
            jedis= redisshardedpool.getjedis();
            result = jedis.setnx(key, value);
        }catch (Exception e){
            log.info("");
            redisshardedpool.returnbrokenresource(jedis);
             return  result;
        }

        return result;
    }




//只有getset 是原子性的
  public static String getset(String key,String value){
        ShardedJedis jedis= null;
        String result = null;
        try {
              jedis= redisshardedpool.getjedis();
              result=jedis.getSet(key,value);
        }catch (Exception e){
            log.info("");
            redisshardedpool.returnbrokenresource(jedis);
        }

  return result;


  }






    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = redisshardedpool.getjedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            redisshardedpool.returnbrokenresource(jedis);
            return result;
        }
        redisshardedpool.returnresource(jedis);
        return result;
    }







    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = redisshardedpool.getjedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
        redisshardedpool.returnbrokenresource(jedis);
            return result;
        }
        redisshardedpool.returnresource(jedis);
        return result;
    }







    public static String setex(String key,String value ,int extime){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = redisshardedpool.getjedis();
            result = jedis.setex(key,extime,value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error",key,value,e);
            redisshardedpool.returnbrokenresource(jedis);
            return result;
        }
        redisshardedpool.returnresource(jedis);
        return result;
    }






    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = redisshardedpool.getjedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            redisshardedpool.returnbrokenresource(jedis);
            return result;
        }
        redisshardedpool.returnresource(jedis);
        return result;
    }



}
