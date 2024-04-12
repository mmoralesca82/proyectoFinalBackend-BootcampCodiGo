package com.grupo1.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Async
    public boolean saveRedis(String key, String valor, int exp){
        try{
            stringRedisTemplate.opsForValue().set(key, valor);
            stringRedisTemplate.expire(key, exp, TimeUnit.MINUTES);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Async
    public String getFromRedis (String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Async
    public void  deleteKey(String key){
        stringRedisTemplate.delete(key);
    }
}