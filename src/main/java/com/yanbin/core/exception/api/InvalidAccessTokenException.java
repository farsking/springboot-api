package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class InvalidAccessTokenException extends ApplicationException {

    public InvalidAccessTokenException() {
        super(ApplicationErrorCode.InvalidAccessToken.getCode(), ApplicationErrorCode.InvalidAccessToken.getReasoning());
    }
}
