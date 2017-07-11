package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class InvalidSignException extends ApplicationException {
    public InvalidSignException() {
        super(ApplicationErrorCode.InvalidSign.getCode(),
                ApplicationErrorCode.InvalidSign.getReasoning());
    }
}
