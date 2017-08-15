package com.yanbin.service.api.domain;

import com.google.common.base.Preconditions;
import com.yanbin.core.cqrs.event.EventBus;
import com.yanbin.core.utils.SHA256;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.UserExample;
import com.yanbin.model.enums.SeqType;
import com.yanbin.service.api.event.CreateUserEvent;
import com.yanbin.service.api.eventhandler.EventDestination;
import com.yanbin.service.base.BeanUtils;


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
        UserMapper userMapper = BeanUtils.getMapper(SeqType.User);
        EventBus eventBus = BeanUtils.getEventBus();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(mobile);
        Preconditions.checkArgument(userMapper.countByExample(userExample)==0,"用户已存在");
        CreateUserEvent createUserEvent = new CreateUserEvent(name, password, mobile, code, id, WebUtils.Session.getId());
        eventBus.pushEvent(EventDestination.UserCreateEvent,createUserEvent,CreateUserEvent.class);
    }
}
