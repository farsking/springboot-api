package com.yanbin.web;

import com.yanbin.core.content.ApiMethodAttribute;
import com.yanbin.core.web.BaseController;
import com.yanbin.model.param.LoginParam;
import com.yanbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yanbin on 2017/7/10.
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    @RequestMapping(value = "login", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object Login(@RequestBody LoginParam loginParam) {
        return wrapperJsonView(userService.Login(loginParam.getUser(), loginParam.getPassword()));
    }

    @ApiMethodAttribute(nonSignatureValidation = true)
    @RequestMapping(value = "add", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object Login(@RequestBody String name) {
        return wrapperJsonView(userService.addUser(name));
    }
}
