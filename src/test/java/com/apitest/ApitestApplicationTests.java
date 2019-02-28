package com.apitest;

import com.apitest.component.RestCompoent;
import com.apitest.entity.User;
import com.apitest.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ApitestApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testRollback(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setEmail("shayang888@qq.com");
        userRepository.save(user);
        Assert.assertEquals(1, user.getId().intValue());
    }

}
