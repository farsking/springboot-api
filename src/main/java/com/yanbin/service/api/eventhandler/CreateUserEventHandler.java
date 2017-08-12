package com.yanbin.service.api.eventhandler;

import com.google.gson.Gson;
import com.yanbin.core.cqrs.event.EventBus;
import com.yanbin.service.base.ISequence;
import com.yanbin.model.enums.SeqType;
import com.yanbin.service.base.SequenceService;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.User;
import com.yanbin.service.api.event.CreateUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CreateUserEventHandler {
    private UserMapper userMapper;
    private Gson gson;
    private ISequence sequence;
    private EventBus eventUtils;

    @Autowired
    public CreateUserEventHandler(UserMapper userMapper, Gson gson, SequenceService sequence, EventBus eventUtils){
        this.userMapper = userMapper;
        this.gson = gson;
        this.sequence = sequence;
        this.eventUtils = eventUtils;
    }

    @JmsListener(destination = EventDestination.UserCreateEvent)
    public void receiveQueue(String message) {
        CreateUserEvent createUserEvent = gson.fromJson(message,CreateUserEvent.class);
        User user = new User();
        user.setId(sequence.newKey(SeqType.User));
        user.setCode(createUserEvent.getCode());
        user.setMobile(createUserEvent.getMobile());
        user.setName(createUserEvent.getName());
        user.setFailedLogins(0);
        user.setPassword(createUserEvent.getPassword());
        user.setStatusId(1);
        user.setTenantId(0L);
        user.setSession(createUserEvent.getSessionId());
        userMapper.insert(user);
        eventUtils.finishEvent(createUserEvent,"true");
    }


}
