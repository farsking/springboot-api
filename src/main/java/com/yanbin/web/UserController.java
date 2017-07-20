package com.yanbin.web;

import com.google.gson.Gson;
import com.yanbin.core.content.ApiMethodAttribute;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.core.web.BaseController;
import com.yanbin.model.param.LoginParam;
import com.yanbin.model.param.UserMqParam;
import com.yanbin.service.Producer;
import com.yanbin.service.UserService;
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
    private Producer producer;
    private Gson gson;

    @Autowired
    public UserController(UserService userService,Producer producer,Gson gson) {
        this.userService = userService;
        this.producer = producer;
        this.gson = gson;
    }

    @ApiMethodAttribute(nonSessionValidation = true)
    @RequestMapping(value = "login", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object Login(@RequestBody LoginParam loginParam) {
        return wrapperJsonView(userService.Login(loginParam.getUser(), loginParam.getPassword()));
    }

    @ApiMethodAttribute(nonSignatureValidation = true)
    @RequestMapping(value = "add", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object addUser(@RequestBody String name) {
        return wrapperJsonView(userService.addUser(name));
    }

    @ApiMethodAttribute(nonSignatureValidation = true)
    @RequestMapping(value = "add/mq", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public Object addUserMq(@RequestBody String name) {
        UserMqParam userMqParam = new UserMqParam();
        userMqParam.setName(name);
        userMqParam.setSessionId(WebUtils.Session.getId());
        String message = gson.toJson(userMqParam,UserMqParam.class);
        producer.sendMessage(message);
        return wrapperJsonView(null);
    }

    @RequestMapping(value = "search", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public Object search(@RequestParam String name) throws IOException, SolrServerException {
        return wrapperJsonView(userService.solrQueryForMysql(name));
    }





}
