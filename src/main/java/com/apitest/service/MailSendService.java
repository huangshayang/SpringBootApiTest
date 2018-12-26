package com.apitest.service;

import com.apitest.component.MailSendComponent;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.inf.MailSendServiceInf;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author huangshayang
 */
@Service
public class MailSendService implements MailSendServiceInf {

    private final MailSendComponent mailSendComponent;

    @Autowired
    public MailSendService(MailSendComponent mailSendComponent) {
        this.mailSendComponent = mailSendComponent;
    }

    @Override
    public ServerResponse resetPasswordMailService(HttpServletRequest request, String email){
        return mailSendComponent.sendResetPasswordMailHandler(request, email, ConfigConsts.RESET_SUBJECT);
    }

    @Override
    public ServerResponse registerMailService(HttpServletRequest request, String email){
        return mailSendComponent.sendRegisterMailHandler(request, email, ConfigConsts.REGESTER_SUBJECT);
    }
}
