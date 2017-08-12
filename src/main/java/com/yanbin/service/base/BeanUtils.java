package com.yanbin.service.base;

import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebSessionManager;
import com.yanbin.core.cqrs.domain.Repository;
import com.yanbin.core.cqrs.event.EventBus;
import com.yanbin.model.enums.SeqType;

public class BeanUtils {

    public static EventBus getEventBus() {
        return ThreadWebContextHolder.getBean("eventBus");
    }

    public static WebSessionManager getWebSessionManager() {
        return ThreadWebContextHolder.getBean("webSessionManager");
    }

    public static <T> T getMapper(SeqType seqType) {
        return ThreadWebContextHolder.getBean(seqType.getMapper());
    }

    public static Repository getRepository() {
        return ThreadWebContextHolder.getBean("repository");
    }
}
