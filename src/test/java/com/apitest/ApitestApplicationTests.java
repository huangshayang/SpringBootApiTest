package com.apitest;

import com.apitest.component.RestCompoent;
import com.apitest.entity.Apis;
import com.apitest.entity.User;
import com.apitest.mapper.ApiMapper;
import com.apitest.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApitestApplicationTests {

    @Resource
    private ApiMapper apiMapper;

//    @Test
//    @Transactional
//    public void testRollback(){
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword("123");
//        user.setEmail("shayang888@qq.com");
//        userRepository.save(user);
//        Assert.assertEquals(1, user.getId().intValue());
//    }

    @Test
    public void testInject(){
        List<Apis> b = apiMapper.findAllApi();
        System.out.println(b);
    }

}
