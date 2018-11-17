package com.apitest.controller;


import com.apitest.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/register")
    public CompletableFuture<Object> registerController(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "captcha") String captcha){
        return CompletableFuture.completedFuture(registerService.registerService(username, password, captcha));
    }
}
