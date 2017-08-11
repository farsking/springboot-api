package com.yanbin.core.cqrs.command;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class CommandBus {

    private List<ICommandHandler> handlers;

    public CommandBus(List<ICommandHandler> handlers) {
        this.handlers = handlers;
    }

    public <TCommand> void Send(TCommand cmd) throws InterruptedException {
        for (ICommandHandler handler : handlers) {
            Type handerType = handler.getClass().getGenericInterfaces()[0];
            Type temp = ((ParameterizedType) handerType).getActualTypeArguments()[0];
            if (((Class) temp).isInstance(cmd)) {
                handler.handler(cmd);
            }
        }
    }
}
