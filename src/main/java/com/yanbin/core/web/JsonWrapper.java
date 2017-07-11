package com.yanbin.core.web;

/**
 * Created by yanbin on 2017/7/1.
 */
public class JsonWrapper {
    private String code;

    private String msg;

    private Object data;

    public JsonWrapper(Object result) {
        this.data = result;
        code = "0";
        msg = "";
    }

    public JsonWrapper(Object result, String message) {
        this.data = result;
        code = "0";
        msg = message;
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

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }
}
