package com.apitest.api.impl;

import com.apitest.api.MailSendService;
import com.apitest.component.MailSendComponent;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MailSendServiceImpl implements MailSendService {
    private final MailSendComponent mailSendComponent;

    @Autowired
    public MailSendServiceImpl(MailSendComponent mailSendComponent) {
        this.mailSendComponent = mailSendComponent;
    }

    @Override
    public ServerResponse resetPasswordMailService(HttpServletRequest request, String email) {
        return mailSendComponent.sendResetPasswordMailHandler(request, email, ConfigConsts.RESET_SUBJECT);
    }

    @Override
    public ServerResponse registerMailService(HttpServletRequest request, String email) {
        return mailSendComponent.sendRegisterMailHandler(request, email, ConfigConsts.REGESTER_SUBJECT);
    }
}
