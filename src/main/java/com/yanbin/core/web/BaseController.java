package com.yanbin.core.web;

import com.yanbin.core.content.ThreadWebContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by yanbin on 2017/7/1.
 */
public abstract class BaseController {

    /**
     * wrapper JSON view and set message
     * @param object
     * @return
     */
    public Object wrapperJsonView(Object object, String message) {
        return new JsonWrapper(object, message);
    }

    /**
     *  wrapper JSON view
     * @param object
     * @return
     */
    public Object wrapperJsonView(Object object) {
        return new JsonWrapper(object);
    }

    /**
     *   wrapper exception JSON view
     * @param throwable
     * @return
     */
    public Object wrapperExceptionJsonView(Throwable throwable) {
        return new JsonExceptionWrapper(throwable);
    }

    /**
     * get local disk path
     *
     * @return
     */
    protected String getLocalDiskPath() {
        HttpServletRequest request = getRequest();
        return request.getSession().getServletContext().getRealPath("/")
                + File.separator;
    }

    public HttpServletRequest getRequest() {
        return ThreadWebContextHolder.getContext().getRequest();
    }

    public HttpServletResponse getResponse() {
        return ThreadWebContextHolder.getContext().getResponse();
    }

}
