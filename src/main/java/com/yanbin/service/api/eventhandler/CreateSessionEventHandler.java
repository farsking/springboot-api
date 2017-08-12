package com.yanbin.service.api.eventhandler;

import com.google.gson.Gson;
import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebContext;
import com.yanbin.core.content.WebSession;
import com.yanbin.core.content.WebSessionManager;
import com.yanbin.core.cqrs.event.EventBus;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.User;
import com.yanbin.service.api.event.CreateSessionEvent;
import com.yanbin.service.api.event.UpdateUserLoginInfoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CreateSessionEventHandler {
    private WebSessionManager webSessionManager;
    private Gson gson;
    private UserMapper userMapper;
    private EventBus eventUtils;

    @Autowired
    public CreateSessionEventHandler(WebSessionManager webSessionManager, Gson gson, UserMapper userMapper, EventBus eventUtils){
        this.gson = gson;
        this.webSessionManager = webSessionManager;
        this.userMapper = userMapper;
        this.eventUtils = eventUtils;
    }

    @JmsListener(destination = EventDestination.SessionCreateEvent)
    public void receiveQueue(String message) {
        CreateSessionEvent createSessionEvent = gson.fromJson(message, CreateSessionEvent.class);
        WebSession session = new WebSession();
        session.setUserId(createSessionEvent.getUserId());
        session.setUserName(createSessionEvent.getUserName());
        session.setTenantId(createSessionEvent.getTenantId());
        WebContext webContext = ThreadWebContextHolder.getContext();
        if (webContext != null) {
            session.setDeviceId(WebUtils.Session.getDeviceId(webContext.getRequest()));
        }
        webSessionManager.add(session,createSessionEvent.getSessionId(),createSessionEvent.getSecretKey());
        if (webContext != null) {
            ThreadWebContextHolder.getContext().setWebSession(session);
        }
        eventUtils.finishEvent(createSessionEvent,"true");
    }

    @JmsListener(destination = EventDestination.UserUpdateLoginInfoEvent)
    public void updateUserLoginInfo(String message){
        UpdateUserLoginInfoEvent updateUserLoginInfoEvent = gson.fromJson(message,UpdateUserLoginInfoEvent.class);
        User user = userMapper.selectByPrimaryKey(updateUserLoginInfoEvent.getId());
        user.setLoginDate(updateUserLoginInfoEvent.getDate());
        user.setSession(updateUserLoginInfoEvent.getSessionId());
        userMapper.updateByPrimaryKeySelective(user);
        eventUtils.finishEvent(updateUserLoginInfoEvent,"true");
    }
}
