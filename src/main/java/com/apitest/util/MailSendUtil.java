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
    private static ServerResponse serverResponse;

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
    public ServerResponse sendResetPasswordMailHandler(HttpServletRequest request, String email, String subject) {
        if (userRepository.findUserByUsernameOrEmail(email, email) != null) {
            mailHandler(request, email, subject);
        }else {
            serverResponse = new ServerResponse(ErrorEnum.EMAIL_IS_ERROR.getStatus(), ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return serverResponse;
    }

    /**
     * 发送注册邮件
     * @param request
     * @param email
     * @param subject
     * @return
     */
    public ServerResponse sendRegisterMailHandler(HttpServletRequest request, String email, String subject) {
        if (userRepository.findUserByUsernameOrEmail(email, email) == null) {
            mailHandler(request, email, subject);
        }else {
            serverResponse = new ServerResponse(ErrorEnum.EMAIL_IS_ERROR.getStatus(), ErrorEnum.EMAIL_IS_ERROR.getMessage());
        }
        return serverResponse;
    }

    /**
     * 邮件发送设置
     * @param request
     * @param email
     * @param subject
     */
    private void mailHandler(HttpServletRequest request, String email, String subject) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        int emailNum = keyNum(email);
        int ipNum = keyNum(ip);
        int uriNum = keyNum(uri);
        boolean numLessFive = (emailNum < 5 || ipNum < 5) && uriNum < 5;
        //限制同一个ip或者同一个email一天内同一个接口超过5次的请求发送
        if (numLessFive) {
            String token = new BCryptPasswordEncoder().encode(email);
            log.info(request.getHeader("referer"));
            redisTemplate.opsForValue().setIfAbsent(token, email, 5, TimeUnit.MINUTES);
            String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?token="+token;
            sendSimpleTextMail(email, subject, path);
            serverResponse = new ServerResponse(ErrorEnum.EMAIL_SEND_SUCCESS.getStatus(), ErrorEnum.EMAIL_SEND_SUCCESS.getMessage());
            saveMail(subject, path);
            redisTemplate.opsForValue().increment(email, 1);
            redisTemplate.opsForValue().increment(ip, 1);
            redisTemplate.opsForValue().increment(uri, 1);
            redisTemplate.expire(email, 24, TimeUnit.HOURS);
            redisTemplate.expire(ip, 24, TimeUnit.HOURS);
            redisTemplate.expire(uri, 24, TimeUnit.HOURS);
        }else {
            serverResponse = new ServerResponse(ErrorEnum.REQUEST_NUM_FULL.getStatus(), ErrorEnum.REQUEST_NUM_FULL.getMessage());
        }
    }

    private int keyNum(String key){
        return redisTemplate.opsForValue().get(key) == null ? 0 : (Integer)redisTemplate.opsForValue().get(key);
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
