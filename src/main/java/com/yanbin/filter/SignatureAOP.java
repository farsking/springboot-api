package com.yanbin.filter;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.yanbin.core.cache.CacheKeyPrefix;
import com.yanbin.core.cache.ICacheClient;
import com.yanbin.core.cache.RedisClient;
import com.yanbin.core.content.ApiMethodAttribute;
import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebContext;
import com.yanbin.core.exception.api.InvalidSignException;
import com.yanbin.core.exception.api.UsedSignException;
import com.yanbin.core.utils.SHA256;
import com.yanbin.core.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * Created by yanbin on 2017/7/19.
 * 验证签名
 */
@Aspect
@Component
public class SignatureAOP {

    private ICacheClient cacheClient;

    private Gson gson;

    @Autowired
    public SignatureAOP(RedisClient cacheClient, Gson gson) {
        this.cacheClient = cacheClient;
        this.gson = gson;
    }

    @Before("execution(public * com.yanbin.web.*.*(..))")
    public void anyMethod(JoinPoint joinPoint) {

        WebContext webContext = ThreadWebContextHolder.getContext();
        assert webContext != null;
        ApiMethodAttribute apiMethodAttribute = webContext.getMethodAttribute();
        boolean nonSignatureValidation = apiMethodAttribute.nonSignatureValidation();
        boolean nonSessionValidation = apiMethodAttribute.nonSessionValidation();
        Object param = null;
        HttpServletRequest request = webContext.getRequest();
        if (Objects.equals(request.getMethod(), RequestMethod.POST.name())) {
            param = joinPoint.getArgs()[0];
        }
        validSign(request, nonSignatureValidation, nonSessionValidation, param);
    }

    /**
     * 有效的时间间隔
     */
    private final static long VALID_TIME_SPAN = 15 * 60 * 1000;
    private final static String SIGN_KEY = "signature";
    private final static String TIME_KEY = "timeSpan";

    private void validSign(HttpServletRequest request, boolean nonSignatureValidation, boolean nonSessionValidation, Object param) {
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
                if (param == null) {
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
                } else {
                    if (!nonSessionValidation) {
                        String secretKey = WebUtils.Session.getSecretKey();
                        realString = "[" +
                                secretKey +
                                "]" +
                                url + "?" + request.getQueryString() +
                                "[" +
                                gson.toJson(param) +
                                "]";
                    } else {
                        realString = url + "?" + request.getQueryString() + "[" + gson.toJson(param) + "]";
                    }
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
}
