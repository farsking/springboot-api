package com.yanbin.filter;

import com.yanbin.core.cache.config.CacheConfigService;
import com.yanbin.core.cache.config.ConfigType;
import com.yanbin.core.content.WebSession;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.core.web.JsonExceptionWrapper;
import com.yanbin.filter.log.Daily;
import com.yanbin.filter.log.ExceptionInfo;
import com.yanbin.filter.log.TimeoutInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by yanbin on 2017/7/7.
 */
@Component
public class LogService {

    private  MongoTemplate  mongoTemplate;
    private CacheConfigService config;

    @Autowired
    public LogService(MongoTemplate mongoTemplate,CacheConfigService cacheConfig){
        this.mongoTemplate = mongoTemplate;
        this.config = cacheConfig;
    }

    public void insertExcept(JsonExceptionWrapper exception) {
        if (!Boolean.parseBoolean(config.getValue(ConfigType.logging.getKey()))){
            return;
        }
        ExceptionInfo data = new ExceptionInfo();
        data.setCode(exception.getCode());
        data.setMsg(exception.getMsg());
        data.setException(exception.getStackTrace());
        if (MDC.get("profile") != null) {
            data.setProfile(MDC.get("profile"));
        }
        WebSession session = WebUtils.Session.get();
        if (MDC.get("ip") != null) {
            data.setIp(MDC.get("ip"));
        }
        if (MDC.get("url") != null) {
            data.setUrl(MDC.get("url"));
        }
        if (MDC.get("url_body") != null) {
            data.setBody(MDC.get("url_body"));
        }
        if (session != null) {
            if (session.getUserId() != null) {
                data.setUserId(session.getUserId().toString());
            }
            if (session.getUserName() != null) {
                data.setUserName(session.getUserName());
            }
        }
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        mongoTemplate.insert(data);
    }

    void insertVisit(TimeoutInfo data) {
        if (!Boolean.parseBoolean(config.getValue(ConfigType.logging.getKey()))){
            return;
        }
        String configValue = config.getValue(ConfigType.logging.getValue());
        if (!configValue.equals("true"))
            return;
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        try {
            mongoTemplate.insert(data);
        } catch (java.lang.Exception e) {
            config.set(ConfigType.logging, "false");
            e.printStackTrace();
        }
    }

    void insertDaily() {
        if (!Boolean.parseBoolean(config.getValue(ConfigType.logging.getKey()))){
            return;
        }
        Daily data = new Daily();
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (MDC.get("ip") != null) {
            data.setIp(MDC.get("ip"));
        }
        if (MDC.get("profile") != null)
            data.setUserAgent(MDC.get("profile"));
        if (MDC.get("method") != null)
            data.setMethod(MDC.get("method"));
        if (MDC.get("url") != null)
            data.setUrl(MDC.get("url"));
        if (MDC.get("userId") != null)
            data.setUserId(MDC.get("userId"));
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        mongoTemplate.insert(data);
    }

}



