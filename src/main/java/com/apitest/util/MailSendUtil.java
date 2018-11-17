package com.apitest.util;

import com.apitest.error.ErrorEnum;
import com.apitest.inf.MailSendCompoentInf;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author huangshayang
 */
@Component
@Log4j2
public class MailSendUtil implements MailSendCompoentInf {

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

    @Override
    public void sendSimpleTextMail(String mail, String subject, Object content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(user);
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(String.valueOf(content));
        javaMailSender.send(message);
    }

    /**
     * 发送邮件
     * @param email
     * @param subject
     * @param key
     * @return
     */
    public CompletableFuture<Object> sendMailHandler(HttpServletRequest request, String email, String subject, String key) {
        Map<String, Object> map = new HashMap<>(8);
        if (userRepository.findUserByUsernameOrEmail(email, email) != null) {
            createMailCode(email, key);
            String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?token="+redisTemplate.boundHashOps("mail").get(key);
            sendSimpleTextMail(email, subject, path);
            map.put("status", ErrorEnum.EMAIL_SEND_SUCCESS.getStatus());
            map.put("message", ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
        }else {
            map.put("status", ErrorEnum.EMAIL_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return CompletableFuture.completedFuture(map);
    }

    /**
     * 生成验证码，存入redis，过期时间1分钟
     * @param email
     * @param key
     */
    private void createMailCode(String email, String key) {
        String token = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
        //创建注册验证用的code
        redisTemplate.boundHashOps("mail").putIfAbsent(key, token);
        redisTemplate.boundHashOps("mail").putIfAbsent(token, email);
        redisTemplate.expire("mail", 1, TimeUnit.MINUTES);
    }
}
