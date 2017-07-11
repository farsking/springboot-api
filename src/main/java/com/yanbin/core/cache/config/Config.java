package com.yanbin.core.cache.config;

/**
 * Created by yanbin on 2017/7/1.
 */
public class Config {
    private String key;
    private String value;
    private Long lastUpdate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

