package com.yanbin.service.api.commandhandler;

import com.yanbin.service.api.command.LoginCommand;
import com.yanbin.service.api.domain.LoginDomain;

public class LoginHandler implements ICommandHandler<LoginCommand> {
    @Override
    public void handler(LoginCommand cmd) {
        LoginDomain loginDomain = new LoginDomain(cmd.getCode(),cmd.getPassword());
        cmd.setResult(loginDomain.Login());
    }
}
