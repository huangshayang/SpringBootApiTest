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

    /**
     * 邮件发送设置
     * @param request
     * @param email
     * @param subject
     * @param map
     */
    private void mailHandler(HttpServletRequest request, String email, String subject, Map<String, Object> map) {
        String ip = request.getRemoteAddr();
        int emailNum = redisTemplate.opsForValue().get(email) == null ? 0 : (Integer)redisTemplate.opsForValue().get(email);
        int ipNum = redisTemplate.opsForValue().get(ip) == null ? 0 : (Integer)redisTemplate.opsForValue().get(ip);
        //限制同一个ip或者同一个email一天内超过5次的请求发送
        if (emailNum < 5 || ipNum < 5) {
            String token = new BCryptPasswordEncoder().encode(email);
            redisTemplate.opsForValue().setIfAbsent(token, email, 1, TimeUnit.MINUTES);
            String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?token="+token;
            sendSimpleTextMail(email, subject, path);
            map.put("status", ErrorEnum.EMAIL_SEND_SUCCESS.getStatus());
            map.put("message", ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
            saveMail(subject, path);
            redisTemplate.opsForValue().increment(email, 1);
            redisTemplate.opsForValue().increment(ip, 1);
            redisTemplate.expire(email, 24, TimeUnit.HOURS);
            redisTemplate.expire(ip, 24, TimeUnit.HOURS);
        }else {
            map.put("status", ErrorEnum.REQUEST_NUM_FULL.getStatus());
            map.put("message", ErrorEnum.REQUEST_NUM_FULL.getMessage());
        }
    }

    /**
     * 把邮件存到数据库
     * @param subject
     * @param path
     */
    private void saveMail(String subject, String path){
        Mails mails = new Mails();
        mails.setSubject(subject);
        mails.setContent(path);
        mailRepository.save(mails);
    }
}
