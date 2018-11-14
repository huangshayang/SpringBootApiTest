package com.apitest.service;

import com.apitest.configconsts.ConfigConsts;
import com.apitest.inf.MailSendServiceInf;
import com.apitest.util.MailSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


/**
 * @author huangshayang
 */
@Service
@Async
public class MailSendService implements MailSendServiceInf {

    @Value("${spring.mail.username}")
    private String user;

    private final MailSendUtil mailSendUtil;

    @Autowired
    public MailSendService(MailSendUtil mailSendUtil) {
        this.mailSendUtil = mailSendUtil;
    }


    @Override
    public CompletableFuture<Object> resetPasswordMailService(String email){
        String key = "resetCode";
        return mailSendUtil.getObjectCompletableFuture(email, ConfigConsts.RESET_SUBJECT, key);
    }


    @Override
    public CompletableFuture<Object> registerMailService(String email){
        String key = "registerCode";
        return mailSendUtil.getObjectCompletableFuture(email, ConfigConsts.REGESTER_SUBJECT, key);
    }
}
