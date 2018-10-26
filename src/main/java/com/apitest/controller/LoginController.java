package com.apitest.controller;

import com.apitest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login")
    public CompletableFuture<Object> loginController(HttpSession httpSession, @RequestBody Map<String, String> models){
        return CompletableFuture.completedFuture(loginService.loginService(httpSession, models));
    }
}
