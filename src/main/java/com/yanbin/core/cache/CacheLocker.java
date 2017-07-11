package com.yanbin.core.cache;

/**
 * Created by yanbin on 2017/7/10.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

/**
 * Created by yanbin on 2017/1/10.
 */
@Component
public class CacheLocker implements ICacheLock{

    /**
     * 最长时间锁为5分钟
     */
    private final static int maxExpireTime = (int) CacheKeyPrefix.Lock.getTimeout();

    /**
     * 系统时间偏移量5秒，服务器间的系统时间差不可以超过5秒,避免由于时间差造成错误的解锁
     */
    private final static int offsetTime = 5;

    private StringRedisTemplate springRedisTemplate;

    @Autowired
    public CacheLocker(StringRedisTemplate stringLongRedisTemplate) {
        this.springRedisTemplate = stringLongRedisTemplate;
    }

    /**
     * 锁
     *
     * @param key      key
     * @param waitTime 秒 - 最大等待时间，如果还无法获取，则直接失败
     * @param expire   秒- 锁生命周期时间
     * @return true 成功 false失败
     * @throws Exception
     */
    public boolean lock(String key, int waitTime, int expire) throws InterruptedException {
        long start = currentTimeMillis();
        String lock_key = CacheKeyPrefix.Lock.getKey() + key;
        do {
            if (!springRedisTemplate.hasKey(lock_key)) {
                Long currentTime = System.currentTimeMillis();
                springRedisTemplate.opsForValue().set(lock_key, currentTime.toString(),
                        (expire > maxExpireTime) ? maxExpireTime : expire, TimeUnit.SECONDS);
                return true;
            } else { // 存在锁,并对死锁进行修复
                // 上次锁时间
                long lastLockTime = Long.parseLong(springRedisTemplate.opsForValue().get(lock_key));
                // 明确死锁,，再次设定一个合理的解锁时间让系统正常解锁
                if (System.currentTimeMillis() - lastLockTime > (expire + offsetTime) * 1000) {
                    // 原子操作，只需要一次,【任然会发生小概率事件，多个服务同时发现死锁同时执行此行代码(并发),
                    // 为什么设置解锁时间为expire（而不是更小的时间），防止在解锁发送错乱造成新锁解锁】
                    springRedisTemplate.opsForValue().set(lock_key, "999999999", expire);
                }
            }
            if (waitTime > 0) {
                Thread.sleep(500);
            }
        }
        while (waitTime > 0 && (currentTimeMillis() - start) < waitTime * 1000);
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @return
     * @throws Exception
     */
    public boolean unlock(String key) {
        String lock_key = CacheKeyPrefix.Lock.getKey() + key;
        springRedisTemplate.delete(lock_key);
        return true;
    }
}

