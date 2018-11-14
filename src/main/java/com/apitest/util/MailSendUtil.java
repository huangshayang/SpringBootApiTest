package com.apitest.util;

import com.apitest.error.ErrorEnum;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author huangshayang
 */
@Component
public class MailSendUtil {

    @Value("${spring.mail.username}")
    private String user;

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    @Autowired
    public MailSendUtil(JavaMailSender javaMailSender, RedisTemplate<String, Object> redisTemplate, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    private void sendMailService(String mail, String subject, Object content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(user);
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(String.valueOf(content));
        javaMailSender.send(message);
    }

    public CompletableFuture<Object> getObjectCompletableFuture(String email, String subject, String key) {
        Map<String, Object> map = new HashMap<>(8);
        if (userRepository.findUserByUsernameOrEmail(email, email) != null) {
            mailTokenAndCode(email, key);
            sendMailService(email, subject, redisTemplate.boundHashOps("mail").get(key));
            map.put("status", ErrorEnum.EMAIL_SEND_SUCCESS.getStatus());
            map.put("message", ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
        }else {
            map.put("status", ErrorEnum.EMAIL_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return CompletableFuture.completedFuture(map);
    }

    private void mailTokenAndCode(String email, String key) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        //创建注册验证用的code
        redisTemplate.boundHashOps("mail").putIfAbsent(key, code);
        redisTemplate.boundHashOps("mail").putIfAbsent(code, email);
        redisTemplate.expire("mail", 5, TimeUnit.MINUTES);
    }
}
