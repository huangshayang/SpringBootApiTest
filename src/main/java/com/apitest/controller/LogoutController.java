package com.apitest.controller;


import com.apitest.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    private LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @DeleteMapping(value = "/logout")
    public Callable<Object> logoutController(HttpSession httpSession){
        return () -> logoutService.logoutService(httpSession);
    }

}
