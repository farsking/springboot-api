package com.yanbin.filter.log;

/**
 * Created by yanbin on 2017/7/10.
 */

public class TimeoutInfo {
    private String ip;
    private String userId;
    private String method;
    private String parameter;
    private String createdTime;
    private String url;
    private String day;
    private String sessionId;
    private Long timeSpan;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String body;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createTime) {
        this.createdTime = createTime;
    }

    public Long getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(Long timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

}

