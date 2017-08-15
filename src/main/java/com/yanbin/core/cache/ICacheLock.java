package com.yanbin.core.cache;

/**
 * Created by yanbin on 2017/7/10.
 */
public interface ICacheLock {

    boolean lock(String key, int waitTime, int expire) throws InterruptedException;

    boolean unlock(String key);
}
