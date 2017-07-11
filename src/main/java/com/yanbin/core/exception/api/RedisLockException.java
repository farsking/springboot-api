package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/10.
 */
public class RedisLockException extends ApplicationException{

    public RedisLockException() {
        super(ApplicationErrorCode.RedisLockException.getCode(), ApplicationErrorCode.RedisLockException.getReasoning());
    }
}
