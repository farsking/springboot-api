package com.yanbin.core.cache;

/**
 * Created by yanbin on 2017/7/1.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient implements ICacheClient {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisClient(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public String getAndTouch(String key, int expires) {
        stringRedisTemplate.expire(key,expires, TimeUnit.SECONDS);
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value, int expires) {
        stringRedisTemplate.opsForValue().set(key,value,expires,TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void touch(String key, int expires) {
        stringRedisTemplate.expire(key,expires,TimeUnit.SECONDS);
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 批量删除keys
     *
     * @param prefix key 前缀
     */
    @Override
    public void deleteKeys(String prefix) {
        Set<String> keys = stringRedisTemplate.keys(prefix+"%");
        for(String key :keys){
            stringRedisTemplate.delete(key);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }
}

