package com.yanbin.core.cqrs;

import com.google.gson.Gson;
import com.yanbin.core.content.ThreadWebContextHolder;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Destination;
import java.lang.reflect.Type;

public class DomainUtils {

    public static <E> void pushEvent(String destinationPath,E event,Type typeofSrc){
        Gson gson = ThreadWebContextHolder.getBean("gson");
        JmsMessagingTemplate jmsMessagingTemplate = ThreadWebContextHolder.getBean("jmsMessagingTemplate");
        Destination destination = new ActiveMQQueue(destinationPath);
        jmsMessagingTemplate.convertAndSend(destination, gson.toJson(event, typeofSrc));
    }
}
