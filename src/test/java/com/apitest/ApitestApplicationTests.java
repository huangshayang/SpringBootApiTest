package com.apitest;

import com.apitest.service.MailSendService;
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

import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApitestApplicationTests {

//    @Autowired
//    private MailSendService mailSendService;

    @Test
    public void contextLoads() {
//        String subject = "Jerome的文本邮件";
//        String content = "Jerome的文本邮件的正文部分";
//        String toUser = "shayang888@qq.com";
//        mailSendService.sendTextMail(toUser, subject, content);
    }

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void set(){
        Optional<Boolean> b = Optional.ofNullable(null);
        System.out.println(b);
        b.ifPresent(aBoolean -> System.out.println(aBoolean.booleanValue()));
    }

}
