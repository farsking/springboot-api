package com.yanbin.core.content;

import com.google.gson.Gson;
import com.yanbin.core.cache.CacheKeyPrefix;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import com.yanbin.core.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by yanbin on 2017/7/1.
 */
@Component
public class WebSessionManager {

    private int timeout;

    private ICacheClient cacheClient;

    @Autowired
    public WebSessionManager(RedisClient cacheClient){
        this.cacheClient = cacheClient;
    }

    /**
     * 每个用户只允许一个会话。
     */
    private boolean singlePerUser;


    public int getTimeout() {
        return (int)CacheKeyPrefix.UserSession.getTimeout();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private String getCacheKeyPrefix() {
        return CacheKeyPrefix.UserSession.getKey();
    }

    public boolean getSinglePerUser() {
        return singlePerUser;
    }

    public void setSinglePerUser(boolean singlePerUser) {
        this.singlePerUser = singlePerUser;
    }

    /**
     * 新增 Web 会话。
     *
     * @param webSession Web 会话
     * @return Web 会话。
     */
    public WebSession add(WebSession webSession) {
        String sessionId = newId(webSession);
        webSession.setSecretKey(newSecretKey());
        return this.add(webSession, sessionId);
    }

    /**
     * 新增 Web 会话。
     *
     * @param webSession Web 会话
     * @return Web 会话。
     */
    private WebSession add(WebSession webSession, String sessionId) {
        webSession.setId(sessionId);
        String cacheKey = getCacheKey(webSession.getId());
        cacheClient.set(cacheKey, new Gson().toJson(webSession, WebSession.class), (int)CacheKeyPrefix.UserSession.getTimeout());
        return webSession;
    }

    /**
     * 删除 Web 会话。
     */
    public void delete(String sessionId) {
        String cacheKey = getCacheKey(sessionId);
        cacheClient.delete(cacheKey);
    }

    /**
     * 获取 Web 会话。
     *
     * @param sessionId Web 会话 ID
     * @return Web 会话。
     */
    public WebSession get(String sessionId) {
        String cacheKey = getCacheKey(sessionId);
        String cacheValue = cacheClient.get(cacheKey);
        WebSession webSession = new Gson().fromJson(cacheValue, WebSession.class);
        if (null != webSession) {
            cacheClient.touch(cacheKey, getTimeout());
        }
        return webSession;
    }

    private String newSecretKey() {
        return UuidUtil.newUuidString();
    }

    private String getCacheKey(String sessionId) {
        String key;
        // 如每用户只允许一个会话，会话ID中保存用户ID（32位）
        // 占用后40位，其中最后8位保留 UUID 数据
        if (singlePerUser) {
            String userIdString = sessionId.substring(sessionId.length() - 10,
                    sessionId.length() - 2);
            long userId = Long.parseLong(userIdString, 16);
            key = getCacheKeyPrefix() + userId;
        } else {
            key = getCacheKeyPrefix() + sessionId;
        }
        return key;
    }

    private String newId(WebSession webSession) {
        UUID uuid = UUID.randomUUID();
        long least = uuid.getLeastSignificantBits();
        // 如每用户只允许一个会话，保存用户ID（32位）
        // 占用后40位，其中最后8位保留 UUID 数据
        if (singlePerUser) {
            least = least & 0xffffff00000000ffL | (webSession.getUserId() << 8);
        }
        return Long.toHexString(uuid.getMostSignificantBits())
                + Long.toHexString(least);
    }
}
