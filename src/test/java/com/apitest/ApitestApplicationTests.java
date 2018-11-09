package com.apitest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApitestApplicationTests {

    @Test
    public void contextLoads() {
    }

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Test
//    @SuppressWarnings("unchecked")
    public void set(){
//        redisTemplate.opsForValue().set("test","testValue");
//        System.out.println(redisTemplate.opsForValue().get("123"));
    }

}
