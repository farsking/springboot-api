package com.yanbin.junit;

import com.google.gson.Gson;
import com.yanbin.Application;
import com.yanbin.core.utils.SHA256;
import com.yanbin.model.param.LoginParam;
import com.yanbin.service.Producer;
import com.yanbin.service.UserService;
import com.yanbin.web.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yanbin on 2017/7/18.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class HttpTest   {

    @Autowired
    UserService userService;

    @Autowired
    Gson gson;

    @Autowired
    Producer producer;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new UserController(userService,producer,gson)).build();
    }

    @Test
    public void indexControllerTest() throws Exception {
        Date now = new Date();
        LoginParam loginParam = new LoginParam();
        loginParam.setUser("yanbin");
        loginParam.setPassword("kpJUiLKKsSWErI/KqKJ6D0l7LGKUDI9PvI7xnryHxD4=");
        String loginString = gson.toJson(loginParam,LoginParam.class);
        String url="/user/login?timeSpan="+String.valueOf(now.getTime())+"["+loginString+"]";
        String signature = SHA256.encrypt(url);
        RequestBuilder requestBuilder = post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("signature",signature)
                .content(loginString);
        mvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
    }

}
