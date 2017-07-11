package com.yanbin.core.sequence;

/**
 * Created by yanbin on 2017/7/8.
 */
public enum SeqType {
    User("ba_user", ""),
    ;

    private String key;
    private String codePrefix;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCodePrefix() {
        return codePrefix;
    }

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    SeqType( String key, String codePrefix) {
        this.key = key;
        this.codePrefix = codePrefix;
    }
}

