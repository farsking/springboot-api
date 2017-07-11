package com.yanbin.core.cache.config;

import com.google.common.collect.Maps;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yanbin on 2017/7/1.
 * 缓存配置服务
 */
@Component
public class CacheConfigService {

    private static Map<String, Config> configs = Maps.newHashMap();

    private ICacheClient cacheClient;

    @Autowired
    public CacheConfigService(RedisClient cacheClient){
        this.cacheClient = cacheClient;
        init();
    }

    private boolean isInit;

    public CacheConfigService(){
        init();
    }

    private void init() {
        if (!isInit) {
            ConfigType[] types = ConfigType.values();
            for (ConfigType configType : types) {
                String value = cacheClient.get(configType.getKey());
                Config config = new Config();
                config.setKey(configType.getKey());
                config.setLastUpdate(System.currentTimeMillis());
                if (!StringUtils.isBlank(value)) {
                    config.setValue(value);
                } else {
                    config.setValue(configType.getValue());
                }
                configs.put(configType.getKey(), config);
            }
            isInit = true;
        }
    }


    public void set(ConfigType type,String value) {
        if (StringUtils.isBlank(value))
            return;
        Config config = new Config();
        config.setKey(type.getKey());
        config.setValue(value);
        config.setLastUpdate(System.currentTimeMillis());
        configs.put(type.getKey(), config);
        cacheClient.set(type.getKey(),type.getValue());
    }

    public String getValue(String key) {
        if (canGetConfig(key))
            return getCacheValue(key);
        else
            return getMapValue(key);
    }

    private boolean canGetConfig(String key) {
        Long end = System.currentTimeMillis();
        Config config = configs.get(key);
        Long timeSpan = (long) 5 * 60 * 1000;
        return (end - config.getLastUpdate()) > timeSpan;
    }

    private String getMapValue(String key) {
        if (configs.containsKey(key))
            return configs.get(key).getValue();
        else
            return null;
    }

    private String getCacheValue(String key) {
        String result = cacheClient.get(key);
        Config config = configs.get(key);
        config.setLastUpdate(System.currentTimeMillis());
        config.setKey(key);
        config.setValue(result);
        return result;
    }
}
