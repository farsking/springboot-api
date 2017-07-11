package com.yanbin.core.sequence;

/**
 * Created by yanbin on 2017/7/8.
 */
public class Sequence {
    private String key;
    private Long nextValue;

    public Sequence(String key,Long nextValue){
        this.key = key;
        this.nextValue = nextValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getNextValue() {
        return nextValue;
    }

    public void setNextValue(Long nextValue) {
        this.nextValue = nextValue;
    }
}
