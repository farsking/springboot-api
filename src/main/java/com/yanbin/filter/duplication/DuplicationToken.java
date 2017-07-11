package com.yanbin.filter.duplication;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yanbin on 2017/7/1.
 */
public class DuplicationToken {
    private String token;

    private AtomicBoolean submited = new AtomicBoolean(false);

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the submited
     */
    public AtomicBoolean getSubmited() {
        return submited;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @param submited the submited to set
     */
    public void setSubmited(AtomicBoolean submited) {
        this.submited = submited;
    }

}
