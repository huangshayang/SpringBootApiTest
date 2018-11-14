package com.apitest.controller;


import com.apitest.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/register")
    public CompletableFuture<Object> registerController(@RequestParam String usernameOrEmail, @RequestParam String password, @RequestParam String captcha){
        return CompletableFuture.completedFuture(registerService.registerService(usernameOrEmail, password, captcha));
    }

//    @GetMapping(value = "/register/mail")
//    public CompletableFuture<Object> registerMailController(@RequestParam String email){
//        return CompletableFuture.completedFuture(registerService.registerMailService(email));
//    }
}
