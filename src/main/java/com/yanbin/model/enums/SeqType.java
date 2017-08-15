package com.yanbin.model.enums;

/**
 * Created by yanbin on 2017/7/8.
 */
public enum SeqType {
    User("ba_user", "userMapper"),
    ;

    private String key;
    private String mapper;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    SeqType( String key, String mapper) {
        this.key = key;
        this.mapper = mapper;
    }
}

