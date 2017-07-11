package com.yanbin.service;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * Created by yanbin on 2017/7/11.
 */
@Service("producer")
public class Producer {

    private JmsMessagingTemplate jmsTemplate;

    private Destination destination;
    @Autowired
    public Producer(JmsMessagingTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
        destination = new ActiveMQQueue("springboot.queue.user");
    }

    public void sendMessage(final String message){
        jmsTemplate.convertAndSend(destination, message);
    }
}
