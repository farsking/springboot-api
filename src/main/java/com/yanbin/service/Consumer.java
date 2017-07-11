package com.yanbin.service;

import com.google.gson.Gson;
import com.yanbin.model.param.UserMqParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by yanbin on 2017/7/11.
 */
@Component
public class Consumer {

    private UserService userService;
    private Gson gson;

    @Autowired
    public Consumer(UserService userService,Gson gson){
        this.userService = userService;
        this.gson = gson;
    }

    @JmsListener(destination = "springboot.queue.user")
    public void receiveQueue(String text) {
        UserMqParam param = gson.fromJson(text, UserMqParam.class);
        userService.addUser(param.getName(),param.getSessionId());
    }
}
