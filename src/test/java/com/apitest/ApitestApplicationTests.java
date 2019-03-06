package com.apitest;

import com.apitest.component.EnvComponent;
import com.apitest.component.RestCompoent;
import com.apitest.entity.Apis;
import com.apitest.entity.Enviroment;
import com.apitest.entity.Task;
import com.apitest.entity.User;
import com.apitest.mapper.ApiMapper;
import com.apitest.mapper.EnvMapper;
import com.apitest.mapper.TaskMapper;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApitestApplicationTests {

    @Resource
    private TaskMapper taskMapper;

    private static Enviroment enviroment;

    @Resource
    private EnvComponent envComponent;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private EnvMapper envMapper;

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
        List<Apis> apisList;
        String b = taskMapper.findById(1).get().getApiIdList();
        String[] arr = b.split(",");
//        List<Integer> ids =Arrays.stream(arr).map(s->Integer.parseInt(s.trim())).
//                collect(Collectors.toList());
//        apisList = apiMapper.findApiByIds(b);
//        System.out.println(apiMapper.findById(Integer.parseInt(arr[0])));
        System.out.println(apiMapper.findApiByIds(taskMapper.findById(1).get().getApiIdList()));
    }

    public Enviroment getEnviroment(int envId) {
        Optional<Enviroment> envOptional = envMapper.findById(envId);
        envOptional.ifPresent(env -> enviroment = env);
        return enviroment;
    }

    @Test
    public void testEnv(){
        System.out.println(EnvComponent.getEnviroment(1));
    }

}
