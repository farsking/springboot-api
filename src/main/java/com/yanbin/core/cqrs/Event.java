package com.yanbin.core.cqrs;

import com.yanbin.core.utils.UuidUtil;

public class Event {
    private String cacheId;

    public Event(String name) {
        cacheId = name + "_" + UuidUtil.newUuidString();
    }

    public String getCacheId() {
        return cacheId;
    }
}
