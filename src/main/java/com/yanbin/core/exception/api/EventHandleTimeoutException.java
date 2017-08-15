package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

public class EventHandleTimeoutException extends ApplicationException {

    public EventHandleTimeoutException() {
        super(ApplicationErrorCode.EventHandlerTimeout.getCode(), ApplicationErrorCode.EventHandlerTimeout.getReasoning());
    }
}
