package com.yanbin.service.api.event;

import com.yanbin.core.cqrs.event.Event;

public class CreateSessionEvent extends Event {
    private String sessionId;
    private Long userId;
    private Long tenantId;
    private String code;
    private String deviceId;
    private String secretKey;
    private String userName;

    public CreateSessionEvent(String sessionId, Long userId, String userName,Long tenantId, String code, String deviceId,String secretKey) {
        super("CreateSessionEvent");
        this.sessionId = sessionId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.code = code;
        this.deviceId = deviceId;
        this.secretKey = secretKey;
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getCode() {
        return code;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getUserName() {
        return userName;
    }
}
