package com.apitest.controller;

import com.apitest.entity.User;
import com.apitest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    private LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login")
    public Callable<Object> loginController(@RequestBody Map<String, Object> models, HttpSession httpSession){
        return () -> loginService.loginService(models, httpSession);
    }

    @PostMapping(value = "/login1")
    public Callable<Object> loginController1(@RequestBody User user, HttpSession httpSession){
        return () -> loginService.loginService1(user, httpSession);
    }

}
