package com.apitest.controller;

import com.apitest.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping(value = "/account")
public class CaptchaController {
    private final CaptchaService captchaService;

    @Autowired
    private CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping(value = "/verify")
    public void captchaController(HttpServletResponse response , HttpSession httpSession) throws IOException {
        captchaService.captchaService(response, httpSession);
    }
}
