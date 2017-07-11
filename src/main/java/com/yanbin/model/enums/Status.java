package com.yanbin.model.enums;

/**
 * Created by yanbin on 2017/7/10.
 */
public enum Status {
    Vaild(1, "启用"),
    Invaild(0, "禁用"),
    Delete(-1, "删除"),;

    private Integer value;
    private String desc;

    Status(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Status parse(Integer value) {
        if (null == value) {
            return null;
        }
        Status[] coll = values();
        for (Status item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
