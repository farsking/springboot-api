package com.yanbin.core.cqrs.command;

public interface ICommandHandler<T> {
    void handler(T cmd) throws InterruptedException;
}
