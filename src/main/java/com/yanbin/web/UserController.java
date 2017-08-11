package com.yanbin.web;

import com.yanbin.core.content.ApiMethodAttribute;
import com.yanbin.core.web.BaseController;
import com.yanbin.model.param.LoginParam;
import com.yanbin.model.param.UserParam;
import com.yanbin.service.UserService;
import com.yanbin.service.api.command.CreateUserCommand;
import com.yanbin.service.api.command.LoginCommand;
import com.yanbin.core.cqrs.command.CommandBus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by yanbin on 2017/7/10.
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private UserService userService;
    private CommandBus commandBus;

    @Autowired
    public UserController(UserService userService, CommandBus commandBus) {
        this.userService = userService;
        this.commandBus = commandBus;
    }

    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    @RequestMapping(value = "login", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object Login(@RequestBody LoginParam loginParam) {
        LoginCommand loginCommand = new LoginCommand(loginParam.getUser(),loginParam.getPassword());
        commandBus.Send(loginCommand);
        return wrapperJsonView(loginCommand.getResult());
    }

    @ApiMethodAttribute(nonSignatureValidation = true)
    @RequestMapping(value = "add/mq", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object addUserMq(@RequestBody UserParam userParam) {
        CreateUserCommand createUserCommand = new CreateUserCommand(userParam.getName(), userParam.getMobile());
        commandBus.Send(createUserCommand);
        return wrapperJsonView(createUserCommand.getResult());
    }

    @RequestMapping(value = "{id}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object getByUserId(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @RequestMapping(value = "search", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public Object search(@RequestParam String name) throws IOException, SolrServerException {
        return wrapperJsonView(userService.solrQueryForMysql(name));
    }

}
