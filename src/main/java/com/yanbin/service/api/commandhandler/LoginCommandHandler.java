package com.yanbin.service.api.commandhandler;

import com.yanbin.core.cqrs.command.ICommandHandler;
import com.yanbin.service.api.command.LoginCommand;
import com.yanbin.service.api.domain.LoginDomain;

public class LoginCommandHandler implements ICommandHandler<LoginCommand> {
    @Override
    public void handler(LoginCommand cmd) throws InterruptedException {
        LoginDomain loginDomain = new LoginDomain(cmd.getCode(),cmd.getPassword());
        cmd.setResult(loginDomain.Login());
    }
}
