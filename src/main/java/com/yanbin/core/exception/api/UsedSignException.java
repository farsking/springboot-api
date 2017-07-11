package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class UsedSignException extends ApplicationException {
    public UsedSignException() {
        super(ApplicationErrorCode.UsedSignature.getCode(),
                ApplicationErrorCode.UsedSignature.getReasoning());
    }
}
