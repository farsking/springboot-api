package com.yanbin.service.api.event;

import java.util.Date;

public class UpdateUserLoginInfoEvent {

    private Long id;
    private Date date;
    private String sessionId;

    public UpdateUserLoginInfoEvent(Long id, Date date, String sessionId) {
        this.id = id;
        this.date = date;
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getSessionId() {
        return sessionId;
    }
}
