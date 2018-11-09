package com.apitest.controller;

import com.apitest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CompletableFuture<Object> loginController(@RequestParam String username, @RequestParam String password){
        return CompletableFuture.completedFuture(loginService.loginService(username, password));
    }
}
