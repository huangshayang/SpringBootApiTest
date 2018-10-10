package com.apitest.controller;

import com.apitest.service.ForgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/account")
public class ForgetController {

    private final ForgetService forgetService;

    @Autowired
    private ForgetController(ForgetService forgetService) {
        this.forgetService = forgetService;
    }

    @PostMapping(value = "/forget")
    public Callable<Object> forgetPasswordController(HttpSession httpSession, @RequestBody Map<String, Object> models) {
        return () -> forgetService.forgetPasswordService(httpSession, models);
    }

    @PostMapping(value = "/forget/token")
    public Callable<Object> getTokenController(HttpSession httpSession, @RequestBody Map<String, Object> username) {
        return () -> forgetService.getTokenService(httpSession, username);
    }
}
