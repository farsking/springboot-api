package com.yanbin.core.cache.config;

/**
 * Created by yanbin on 2017/7/1.
 */
public enum ConfigType {
    /**
     * 日志是否启用
     */
    logging("config_logging", "true"),
    /**
     * 访问超时记录时间
     */
    visitTimeOut("config_visitTimeOut", "3000"),;
    private String value;
    private String key;

    ConfigType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue() {
        return value;
    }

}
