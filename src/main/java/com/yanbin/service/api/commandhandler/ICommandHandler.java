package com.yanbin.service.api.commandhandler;

public interface ICommandHandler<T> {
    void handler(T cmd);
}
