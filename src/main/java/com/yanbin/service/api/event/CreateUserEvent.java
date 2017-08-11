package com.yanbin.service.api.event;

import com.yanbin.core.cqrs.Event;

public class CreateUserEvent extends Event{
    private String name;
    private String password;
    private String mobile;
    private String code;
    private Long id;
    private String sessionId;

    public CreateUserEvent(String name, String password, String mobile, String code, Long id,String sessionId) {
        super("CreateUserEvent");
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.code = code;
        this.id = id;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
