package com.yanbin.core.cqrs.event;

import com.google.gson.Gson;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import com.yanbin.core.exception.api.EventHandleTimeoutException;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.lang.reflect.Type;

@Component
public class EventBus {

    private ICacheClient redisClient;
    private Gson gson;
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public EventBus(RedisClient redisClient,Gson gson,JmsMessagingTemplate jmsMessagingTemplate) {
        this.redisClient = redisClient;
        this.gson = gson;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    public <E> void pushEvent(String destinationPath, E event, Type typeofSrc) {
        Destination destination = new ActiveMQQueue(destinationPath);
        jmsMessagingTemplate.convertAndSend(destination, gson.toJson(event, typeofSrc));
    }

    public <E> void pushEvent(String destinationPath, E event, Type typeofSrc, IEventCallback callback) throws InterruptedException {
        Destination destination = new ActiveMQQueue(destinationPath);
        jmsMessagingTemplate.convertAndSend(destination, gson.toJson(event, typeofSrc));

        String result;
        Event e = (Event) event;
        int count = 0;
        while (true) {
            Thread.sleep(1000);
            result = redisClient.get(e.getCacheId());
            if (StringUtils.isNotBlank(result)) {
                break;
            }
            count++;
            if (count>=30){
                throw new EventHandleTimeoutException();
            }
        }
        callback.callback(result);
    }

    public void finishEvent(Event event, String result) {
        redisClient.set(event.getCacheId(), result, 60);
    }
}
