package com.apitest.controller;

import com.apitest.service.MailSendService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MailSendController {

    private final MailSendService mailSendService;

    @Autowired
    public MailSendController(MailSendService mailSendService) {
        this.mailSendService = mailSendService;
    }

    @GetMapping(value = "/reset/mail")
    public ServerResponse resetPasswordMailController(HttpServletRequest request, @RequestParam(name = "email") String email) {
        return mailSendService.resetPasswordMailService(request, email);
    }

    @GetMapping(value = "/register/mail")
    public ServerResponse registerMailController(HttpServletRequest request, @RequestParam(name = "email") String email) {
        return mailSendService.registerMailService(request, email);
    }
}
