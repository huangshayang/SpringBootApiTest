package com.apitest.controller;

import com.apitest.service.ForgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/account")
public class ForgetController {

    private final ForgetService forgetService;

    @Autowired
    public ForgetController(ForgetService forgetService) {
        this.forgetService = forgetService;
    }

    @PutMapping(value = "/reset/password")
    public CompletableFuture<Object> resetPasswordService(@RequestParam String newPassword, @RequestParam String code) {
        return CompletableFuture.completedFuture(forgetService.resetPasswordService(newPassword, code));
    }

//    @GetMapping(value = "/reset/mail")
//    public CompletableFuture<Object> resetPasswordMailController(@RequestParam String email){
//        return CompletableFuture.completedFuture(forgetService.resetPasswordMailService(email));
//    }
}
