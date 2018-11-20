package com.apitest.service;

import com.apitest.configconsts.ConfigConsts;
import com.apitest.inf.MailSendServiceInf;
import com.apitest.util.MailSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author huangshayang
 */
@Service
public class MailSendService implements MailSendServiceInf {

    @Value("${spring.mail.username}")
    private String user;

    private final MailSendUtil mailSendUtil;

    @Autowired
    public MailSendService(MailSendUtil mailSendUtil) {
        this.mailSendUtil = mailSendUtil;
    }


    @Override
    public Object resetPasswordMailService(HttpServletRequest request, String email){
        String key = "resetToken";
        return mailSendUtil.sendResetPasswordMailHandler(request, email, ConfigConsts.RESET_SUBJECT, key);
    }


    @Override
    public Object registerMailService(HttpServletRequest request, String email){
        String key = "registerToken";
        return mailSendUtil.sendRegisterMailHandler(request, email, ConfigConsts.REGESTER_SUBJECT, key);
    }
}
