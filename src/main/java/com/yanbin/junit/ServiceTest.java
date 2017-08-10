package com.yanbin.junit;

import com.yanbin.Application;
import com.yanbin.dao.model.User;
import com.yanbin.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by yanbin on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class ServiceTest {

    @Autowired
    private UserService userService;

    public ServiceTest(){

    }

    @Test
    public void test(){
        User loginDTO = userService.getUser(10L);
        Assert.assertEquals(java.util.Optional.ofNullable(loginDTO.getId()),10L);
    }

}
