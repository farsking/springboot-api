package com.yanbin.core.cache;


/**
 * Created by yanbin on 2017/7/1.
 */
public interface ICacheClient {
    String get(String key);

    String getAndTouch(String key, int expires);

    void set(String key,String value, int expires);

    void set(String key,String value);

    void touch(String key, int expires);

    void delete(String key);

    void deleteKeys(String prefix);

    boolean hasKey(String key);

}
