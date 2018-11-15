package com.apitest.controller;

import com.apitest.service.MailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/account")
public class MailSendController {

    private final MailSendService mailSendService;

    @Autowired
    public MailSendController(MailSendService mailSendService) {
        this.mailSendService = mailSendService;
    }

    @GetMapping(value = "/reset/mail")
    public CompletableFuture<Object> resetPasswordMailController(@RequestParam(name = "email") String email){
        return CompletableFuture.completedFuture(mailSendService.resetPasswordMailService(email));
    }

    @GetMapping(value = "/register/mail")
    public CompletableFuture<Object> registerMailController(@RequestParam(name = "email") String email){
        return CompletableFuture.completedFuture(mailSendService.registerMailService(email));
    }
}
