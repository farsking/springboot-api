package com.yanbin.filter;

import com.google.gson.Gson;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import com.yanbin.core.exception.api.DuplicationException;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.filter.duplication.DuplicationAnnotation;
import com.yanbin.filter.duplication.DuplicationProcessor;
import com.yanbin.filter.duplication.DuplicationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by yanbin on 2017/7/1.
 * 重复提交拦截器
 */
@Component
public class DuplicationInterceptor extends HandlerInterceptorAdapter {

    private static final int AVOID_DUPLICATE_TIME = 6;

    private ICacheClient cacheClient;

    @Autowired
    public DuplicationInterceptor(RedisClient redisClient){
        cacheClient = redisClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Gson gson = new Gson();
            DuplicationAnnotation annotation = method.getAnnotation(DuplicationAnnotation.class);
            if (null != annotation) {
                String sessionId = WebUtils.Session.get().getId();
                boolean validateToken = annotation.validateToken();
                if (validateToken) {
                    String cacheKey = DuplicationProcessor.getCacheKey(sessionId, annotation.businessType());
                    DuplicationToken token = gson.fromJson(cacheClient.get(cacheKey), DuplicationToken.class);
                    if (token == null) {
                        token = new DuplicationToken();
                        token.getSubmited().set(true);
                        String jsonObject = gson.toJson(token);
                        cacheClient.set(cacheKey, jsonObject, AVOID_DUPLICATE_TIME);
                    } else {
                        throw new DuplicationException();
                    }
                }
            }
        }
        return true;
    }
}

