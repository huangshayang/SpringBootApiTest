package com.apitest.controller;


import com.apitest.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @DeleteMapping(value = "/logout")
    public CompletableFuture<Object> logoutController(){
        return CompletableFuture.completedFuture(logoutService.logoutService());
    }

}
