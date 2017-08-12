package com.yanbin.core.cqrs.domain;

import com.google.gson.Gson;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Repository {

    private ICacheClient redisClient;
    private Gson gson;

    @Autowired
    public Repository(RedisClient redisClient, Gson gson) {
        this.redisClient = redisClient;
        this.gson = gson;
    }

    public <T> T Load(String id, Class<T> classOfT) {
        return gson.fromJson(redisClient.get(id), classOfT);
    }

    public <T> void save(Base object, Class<T> classOfT) {
        redisClient.set(object.getDomainId(),gson.toJson(object, classOfT));
    }
}
