package com.apitest.component;

import com.apitest.entity.Mails;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.MailMapper;
import com.apitest.mapper.UserMapper;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author huangshayang
 */
@Component
@Log4j2
public class MailSendComponent {

    @Value("${spring.mail.username}")
    private String user;

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    private static ServerResponse serverResponse;


    @Autowired
    public MailSendComponent(JavaMailSender javaMailSender, RedisTemplate<String, Object> redisTemplate) {
        this.javaMailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
    }

    @Resource
    private UserMapper userMapper;

    @Resource
    private MailMapper mailMapper;

    private void sendSimpleTextMail(String mail, String subject, Object content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(user);
            message.setTo(mail);
            message.setSubject(subject);
            message.setText(String.valueOf(content));
            javaMailSender.send(message);
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
    }

    /**
     * 发送重置密码邮件
     *
     * @param request 请求
     * @param email   要发送到的邮箱
     * @param subject 邮件主题
     * @return 返回响应对象
     */
    public ServerResponse sendResetPasswordMailHandler(HttpServletRequest request, String email, String subject) {
        try {
            log.info("email: " + email);
            if (userMapper.findUserByUsernameOrEmail(email, email) != null) {
                String token = new BCryptPasswordEncoder().encode(email);
                String path = request.getScheme() + "://" + request.getServerName() + "/reset-password?token=" + token;
                mailHandler(request, email, subject, path, token);
            } else {
                serverResponse = new ServerResponse(ErrorEnum.EMAIL_IS_ERROR.getStatus(), ErrorEnum.EMAIL_IS_ERROR.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    /**
     * 发送注册邮件
     *
     * @param request 请求
     * @param email   要发送到的邮箱
     * @param subject 邮件主题
     * @return 返回响应对象
     */
    public ServerResponse sendRegisterMailHandler(HttpServletRequest request, String email, String subject) {
        try {
            log.info("email: " + email);
            if (userMapper.findUserByUsernameOrEmail(email, email) == null) {
                String token = new BCryptPasswordEncoder().encode(email);
                String path = request.getScheme() + "://" + request.getServerName() + "/register-password?token=" + token;
                mailHandler(request, email, subject, path, token);
            } else {
                serverResponse = new ServerResponse(ErrorEnum.EMAIL_IS_ERROR.getStatus(), ErrorEnum.EMAIL_IS_ERROR.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    /**
     * 邮件发送设置
     *
     * @param request 请求
     * @param email   要发送到的邮箱
     * @param subject 邮件主题
     */
    private void mailHandler(HttpServletRequest request, String email, String subject, String path, String token) {
        try {
            String ip = request.getRemoteAddr();
            String uri = request.getRequestURI();
            long emailNum = redisTemplate.opsForValue().increment(email);
            long ipNum = redisTemplate.opsForValue().increment(ip);
            long uriNum = redisTemplate.opsForValue().increment(uri);
            boolean numLessFive = (emailNum < 6 || ipNum < 6) && uriNum < 6;
            //限制同一个ip或者同一个email一天内同一个接口超过5次的请求发送
            if (numLessFive) {
                redisTemplate.opsForValue().setIfAbsent(token, email, 5, TimeUnit.MINUTES);
                sendSimpleTextMail(email, subject, path);
                saveMail(subject, path);
                redisTemplate.expire(email, 24, TimeUnit.HOURS);
                redisTemplate.expire(ip, 24, TimeUnit.HOURS);
                redisTemplate.expire(uri, 24, TimeUnit.HOURS);
                serverResponse = new ServerResponse(ErrorEnum.EMAIL_SEND_SUCCESS.getStatus(), ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.REQUEST_NUM_FULL.getStatus(), ErrorEnum.REQUEST_NUM_FULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
    }

    /**
     * 把邮件存到数据库
     *
     * @param subject 邮件主题
     * @param path    邮件链接
     */
    private void saveMail(String subject, String path) {
        try {
            Mails mails = new Mails();
            mails.setSubject(subject);
            mails.setContent(path);
            mailMapper.save(mails);
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
    }
}
