package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class DuplicationException extends ApplicationException {

    public DuplicationException() {
        super(ApplicationErrorCode.DuplicateSubmit.getCode(), ApplicationErrorCode.DuplicateSubmit.getReasoning());
    }

    public DuplicationException(Exception e) {
        super(ApplicationErrorCode.DuplicateSubmit.getCode(), e.getMessage());
    }
}
