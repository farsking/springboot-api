package com.yanbin.core.web;

import com.yanbin.core.exception.ApplicationErrorCode;
import com.yanbin.core.exception.ApplicationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by yanbin on 2017/7/5.
 */
public class JsonExceptionWrapper {

    private String code;
    private String msg;
    private Throwable throwable;

    public JsonExceptionWrapper(Throwable ex) {
        if (ex instanceof ApplicationException) {
            ApplicationException gex = (ApplicationException) ex;
            this.code = gex.getCode();
            this.msg = gex.getReasoning();
        } else if (ex instanceof IllegalArgumentException
                || ex instanceof MissingServletRequestParameterException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof HttpRequestMethodNotSupportedException
                || ex instanceof HttpMediaTypeNotSupportedException
                || ex instanceof MethodArgumentTypeMismatchException) {
            this.code = ApplicationErrorCode.ArgumentsIncorrect.getCode();
            this.msg = ex.getMessage();
        } else if (ex instanceof IOException) {
            this.code = ApplicationErrorCode.IOException.getCode();
            this.msg = ex.getMessage();
        } else if (ex instanceof NoHandlerFoundException) {
            this.code = ApplicationErrorCode.NoHandlerFound.getCode();
            this.msg = ApplicationErrorCode.NoHandlerFound.getReasoning();
        } else {
            this.code = ApplicationErrorCode.UnKnowException.getCode();
            this.msg = ApplicationErrorCode.UnKnowException.getReasoning() + ":" + ex.getMessage();
        }
        throwable = ex;
    }

    public String getStackTrace() {
        if (throwable == null) {
            return "";
        }
        return Arrays.toString(throwable.getStackTrace());
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

}
