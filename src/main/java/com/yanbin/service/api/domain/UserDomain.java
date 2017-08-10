package com.yanbin.service.api.domain;

import com.google.gson.Gson;
import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.utils.SHA256;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.service.api.event.CreateUserEvent;
import com.yanbin.service.api.eventhandler.EventDestination;
import org.springframework.jms.core.JmsMessagingTemplate;


public class UserDomain {
    private String name;
    private String mobile;
    private String code;
    private Long id;
    private String password;

    public UserDomain(String name, String mobile, Long id) {
        this.name = name;
        this.mobile = mobile;
        this.id = id;
        this.code = mobile;
        this.password = SHA256.encrypt("888888");
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCode() {
        return code;
    }

    public Long getId() {
        return id;
    }

    public void createUser() {
        CreateUserEvent createUserEvent = new CreateUserEvent(name, password, mobile, code, id, WebUtils.Session.getId());
        DomainUtils.pushEvent(EventDestination.UserCreateEvent,createUserEvent,CreateUserEvent.class);
    }
}
