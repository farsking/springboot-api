package com.yanbin.service.commandhandler;

public interface ICommandHandler<T> {
    void handler(T cmd);
}
