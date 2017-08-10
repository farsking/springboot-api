package com.yanbin.service.api.command;

import com.yanbin.model.dto.LoginDTO;

public class LoginCommand {
    private String code;
    private String password;
    private LoginDTO result;

    public LoginCommand(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }

    public LoginDTO getResult() {
        return result;
    }

    public void setResult(LoginDTO result) {
        this.result = result;
    }
}
