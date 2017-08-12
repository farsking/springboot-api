package com.yanbin.service.api.domain;

import com.google.common.base.Preconditions;
import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebContext;
import com.yanbin.core.content.WebSessionManager;
import com.yanbin.core.cqrs.event.EventBus;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.User;
import com.yanbin.dao.model.UserExample;
import com.yanbin.model.dto.LoginDTO;
import com.yanbin.model.enums.SeqType;
import com.yanbin.service.api.event.CreateSessionEvent;
import com.yanbin.service.api.event.UpdateUserLoginInfoEvent;
import com.yanbin.service.api.eventhandler.EventDestination;
import com.yanbin.service.base.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class LoginDomain {

    private String sessionId;
    private String secretKey;
    private String code;
    private String password;

    public LoginDomain(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LoginDTO Login() throws InterruptedException {
        UserMapper userMapper = BeanUtils.getMapper(SeqType.User);
        EventBus eventBus = BeanUtils.getEventBus();
        WebContext webContext = ThreadWebContextHolder.getContext();
        WebSessionManager webSessionManager = BeanUtils.getWebSessionManager();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(code).andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(users), "用户名或密码不存在");
        Preconditions.checkArgument(users.size() == 1, "账号异常");
        User user = users.get(0);
        CreateSessionEvent createSessionEvent = new CreateSessionEvent(webSessionManager.newId(user.getId()), user.getId(), user.getName(),
                user.getTenantId(), user.getCode(), WebUtils.Session.getDeviceId(webContext.getRequest()), webSessionManager.newSecretKey());
        eventBus.pushEvent(EventDestination.SessionCreateEvent, createSessionEvent, CreateSessionEvent.class, System.out::println);

        UpdateUserLoginInfoEvent updateUserLoginInfoEvent = new UpdateUserLoginInfoEvent(user.getId(), new Date(), createSessionEvent.getSessionId());
        eventBus.pushEvent(EventDestination.UserUpdateLoginInfoEvent, updateUserLoginInfoEvent, UpdateUserLoginInfoEvent.class,System.out::println);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setTokenId(createSessionEvent.getSessionId());
        loginDTO.setCode(user.getCode());
        loginDTO.setName(user.getName());
        loginDTO.setSecretKey(createSessionEvent.getSecretKey());
        return loginDTO;
    }
}
