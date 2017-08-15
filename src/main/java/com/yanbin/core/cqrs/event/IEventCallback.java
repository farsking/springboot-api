package com.yanbin.core.cqrs.event;

public interface IEventCallback {
    void callback(String result);
}
