package com.yanbin.core.exception.api;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class ArgumentsIncorrectException extends ApplicationException {
    private static final long serialVersionUID = -698281893764433657L;

    public ArgumentsIncorrectException() {
        super(ApplicationErrorCode.ArgumentsIncorrect.getCode(), ApplicationErrorCode.ArgumentsIncorrect.getReasoning());
    }

    public ArgumentsIncorrectException(String reasoning) {
        super(ApplicationErrorCode.ArgumentsIncorrect.getCode(), reasoning);
    }
}
