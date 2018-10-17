package com.apitest.controller;

import com.apitest.service.ForgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(value = "/account")
public class ForgetController {

    private final ForgetService forgetService;

    @Autowired
    private ForgetController(ForgetService forgetService) {
        this.forgetService = forgetService;
    }

    @PostMapping(value = "/forget")
    public Object forgetPasswordController(HttpSession httpSession, @RequestBody Map<String, String> models) {
        return forgetService.forgetPasswordService(httpSession, models);
    }

    @PostMapping(value = "/forget/token")
    public Object getTokenController(HttpSession httpSession) {
        return forgetService.getTokenService(httpSession);
    }
}
