package com.apitest.api.impl;

import com.apitest.api.MailSendService;
import com.apitest.component.MailSendComponent;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MailSendServiceImpl implements MailSendService {
    private final MailSendComponent mailSendComponent;

    @Value("${spring.RESET_KEY}")
    private String resetKey;

    @Value("${spring.REGESTER_KEY}")
    private String registerKey;

    @Autowired
    public MailSendServiceImpl(MailSendComponent mailSendComponent) {
        this.mailSendComponent = mailSendComponent;
    }

    @Override
    public ServerResponse resetPasswordMailService(HttpServletRequest request, String email) {
        return mailSendComponent.sendResetPasswordMailHandler(request, email, resetKey);
    }

    @Override
    public ServerResponse registerMailService(HttpServletRequest request, String email) {
        return mailSendComponent.sendRegisterMailHandler(request, email, registerKey);
    }
}
