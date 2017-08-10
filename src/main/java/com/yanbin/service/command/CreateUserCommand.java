package com.yanbin.service.command;

public class CreateUserCommand {
    private String name;
    private String mobile;
    private Long result;

    public CreateUserCommand(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }
}
