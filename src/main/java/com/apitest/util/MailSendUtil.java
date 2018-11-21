package com.apitest.util;

import com.apitest.entity.Mails;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.MailSendCompoentInf;
import com.apitest.repository.MailRepository;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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
    private final MailRepository mailRepository;

    @Autowired
    public MailSendUtil(JavaMailSender javaMailSender, RedisTemplate<String, Object> redisTemplate, UserRepository userRepository, MailRepository mailRepository) {
        this.javaMailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.mailRepository = mailRepository;
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
     * 发送重置密码邮件
     * @param email
     * @param subject
     * @return
     */
    public Object sendResetPasswordMailHandler(HttpServletRequest request, String email, String subject) {
        Map<String, Object> map = new HashMap<>(8);
        if (userRepository.findUserByUsernameOrEmail(email, email) != null) {
            mailHandler(request, email, subject, map);
        }else {
            map.put("status", ErrorEnum.EMAIL_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return map;
    }

    /**
     * 发送注册邮件
     * @param request
     * @param email
     * @param subject
     * @return
     */
    public Object sendRegisterMailHandler(HttpServletRequest request, String email, String subject) {
        Map<String, Object> map = new HashMap<>(8);
        if (userRepository.findUserByUsernameOrEmail(email, email) == null) {
            mailHandler(request, email, subject, map);
        }else {
            map.put("status", ErrorEnum.EMAIL_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return map;
    }

    private void mailHandler(HttpServletRequest request, String email, String subject, Map<String, Object> map) {
        String token = new BCryptPasswordEncoder().encode(email);
        redisTemplate.opsForValue().setIfAbsent(token, email, 5, TimeUnit.MINUTES);
        String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?token="+token;
        sendSimpleTextMail(email, subject, path);
        map.put("status", ErrorEnum.EMAIL_SEND_SUCCESS.getStatus());
        map.put("message", ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
        Mails mails = new Mails();
        mails.setSubject(subject);
        mails.setContent(path);
        mailRepository.save(mails);
    }
}
