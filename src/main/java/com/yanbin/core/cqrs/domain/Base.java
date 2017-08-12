package com.yanbin.core.cqrs.domain;

import com.yanbin.core.utils.UuidUtil;

public class Base {

    public Base(String name){
        domainId = name + "_" + UuidUtil.newUuidString();
    }

    private String domainId;

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

}
