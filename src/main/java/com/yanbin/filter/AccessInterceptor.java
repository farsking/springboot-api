package com.yanbin.filter;

import com.google.common.base.Preconditions;
import com.yanbin.core.cache.CacheKeyPrefix;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import com.yanbin.core.cache.config.CacheConfigService;
import com.yanbin.core.cache.config.ConfigType;
import com.yanbin.core.content.*;
import com.yanbin.core.exception.api.InvalidAccessTokenException;
import com.yanbin.core.exception.api.InvalidSignException;
import com.yanbin.core.exception.api.UsedSignException;
import com.yanbin.core.utils.SHA256;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.filter.log.TimeoutInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * Created by yanbin on 2017/7/1.
 * 访问拦截器
 * 主要验证token signature,获取session信息，记录访问日志等功能
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {

    private WebSessionManager webSessionManager;

    private ICacheClient cacheClient;

    private LogService logService;

    private CacheConfigService configService;

    private long beginTime;

    @Autowired
    public AccessInterceptor(WebSessionManager webSessionManager, RedisClient redisClient,LogService logService,CacheConfigService cacheConfigService){
        this.cacheClient = redisClient;
        this.webSessionManager = webSessionManager;
        this.logService = logService;
        this.configService = cacheConfigService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        beginTime = System.currentTimeMillis();
        MDC.put("ip", WebUtils.Http.getIpAddr(request));
        String userAgent = WebUtils.Http.getUserAgent(request);
        MDC.put("profile", null == userAgent ? "EmptyUserAgent" : userAgent);
        if (StringUtils.isBlank(request.getRequestURI())) {
            MDC.put("url", "none");
        } else {
            String url = request.getRequestURI();
            if (StringUtils.isNotBlank(request.getQueryString())) {
                url = url + "?" + request.getQueryString();
            }
            MDC.put("url", url);
        }
        MDC.put("method", WebUtils.Http.getMethod(request));
        WebContext webContext = buildWebContext(request, response);
        boolean nonSessionValidation = false;
        boolean nonSignatureValidation = false;
        if (handler instanceof HandlerMethod) {
            ApiMethodAttribute methodAttribute = ((HandlerMethod) handler).getMethod().getAnnotation(ApiMethodAttribute.class);
            if (null != methodAttribute) {
                nonSessionValidation = methodAttribute.nonSessionValidation();
                nonSignatureValidation = methodAttribute.nonSignatureValidation();
                webContext.setMethodAttribute(methodAttribute);
            }
        }
        if (!request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            WebSession webSession = validationSession(request, webContext, nonSessionValidation);
            loggerUserInfo(webSession);
        }
        validSign(request, nonSignatureValidation, nonSessionValidation);
        String sessionId = WebUtils.Session.getSessionId(request);
        if (!StringUtils.isBlank(sessionId)) {
            MDC.put("sessionId", sessionId);
        }
        logService.insertDaily();
        return true;
    }

    private WebContext buildWebContext(HttpServletRequest request,
                                       HttpServletResponse response) {
        WebContext webContext = new WebContext();
        webContext.setRequest(request);
        webContext.setResponse(response);
        ThreadWebContextHolder.setContext(webContext);
        return webContext;
    }

    private WebSession validationSession(HttpServletRequest request,
                                         WebContext webContext, boolean nonSessionValidation) {
        WebSession webSession = null;
        if (!nonSessionValidation) {
            String sessionId = WebUtils.Session.getSessionId(request);
            if (StringUtils.isBlank(sessionId)) {
                throw new InvalidAccessTokenException();
            }
            webSession = webSessionManager.get(sessionId);
            if (null == webSession || !sessionId.equals(webSession.getId())) {
                throw new InvalidAccessTokenException();
            }
        }
        if (null != webSession) {
            webContext.setWebSession(webSession);
        }
        return webSession;
    }

    /**
     * 有效的时间间隔
     */
    private final static long VALID_TIME_SPAN = 15 * 60 * 1000;
    private final static String SIGN_KEY = "signature";
    private final static String TIME_KEY = "timeSpan";

    private void validSign(HttpServletRequest request, boolean nonSignatureValidation, boolean nonSessionValidation) {
        if (!nonSignatureValidation) {
            String clientSign = request.getHeader(SIGN_KEY);
            if (StringUtils.isBlank(clientSign)) {
                throw new InvalidSignException();
            }
            Preconditions.checkArgument(StringUtils.isNotBlank(request.getParameter(TIME_KEY)), "时间戳不存在");
            long time = Long.parseLong(request.getParameter(TIME_KEY));
            Date now = new Date();
            long timeSpan = Math.abs(now.getTime() - time);
            if (timeSpan <= VALID_TIME_SPAN) {
                if (cacheClient.get(CacheKeyPrefix.ApiSign.getKey() + clientSign) != null) {
                    throw new UsedSignException();
                }
                String realString;
                String url = request.getRequestURI();
                if (!nonSessionValidation) {
                    String secretKey = WebUtils.Session.getSecretKey();
                    realString = "[" +
                            secretKey +
                            "]" +
                            url + "?" + request.getQueryString() +
                            "[" +
                            secretKey +
                            "]";
                } else {
                    realString = url + "?" + request.getQueryString();
                }
                String serverSign = SHA256.encrypt(realString);
                if (!(Objects.equals(serverSign, clientSign))) {
                    throw new InvalidSignException();
                }
            } else {
                throw new InvalidSignException();
            }
            cacheClient.set(CacheKeyPrefix.ApiSign.getKey() + clientSign, String.valueOf(new Date().getTime()), (int) CacheKeyPrefix.ApiSign.getTimeout());
        }
    }


    private void loggerUserInfo(WebSession webSession) {
        if (null != webSession) {
            if (null != webSession.getUserId()) {
                MDC.put("userId", null == webSession.getUserId() ? ""
                        : webSession.getUserId().toString());
            }
            if (null != webSession.getUserName()) {
                MDC.put("userName", webSession.getUserName());
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String accept = request.getHeader("Accept");
        if (MediaType.APPLICATION_JSON_VALUE.equals(accept) || MediaType.ALL_VALUE.equals(accept)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        Method method = ((HandlerMethod) handler).getMethod();
        long endTime = System.currentTimeMillis();
        long timeSpan = 2000L;
        long time = endTime - beginTime;
        String configValue = configService.getValue(ConfigType.visitTimeOut.getKey());
        if (StringUtils.isNumeric(configValue)) {
            timeSpan = Long.parseLong(configValue);
        }

        if (time > timeSpan) {
            TimeoutInfo visit = new TimeoutInfo();
            if (org.slf4j.MDC.get("ip") != null) {
                visit.setIp(org.slf4j.MDC.get("ip"));
            }
            if (null != org.slf4j.MDC.get("userId")) {
                visit.setUserId(org.slf4j.MDC.get("userId"));
            }
            if (null != org.slf4j.MDC.get("url")) {
                visit.setUrl(org.slf4j.MDC.get("url"));
            }
            if (null != org.slf4j.MDC.get("url_body")) {
                visit.setBody(org.slf4j.MDC.get("url_body"));
            }
            visit.setMethod(method.getName());
            //visit.setParameter(Arrays.toString(pjp.getArgs()));
            visit.setTimeSpan(time);
            logService.insertVisit(visit);
        }
    }
}
