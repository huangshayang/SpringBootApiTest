package com.apitest.controller;

import com.apitest.api.ResetPasswordService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PutMapping(value = "/reset/password")
    public ServerResponse resetPasswordService(@RequestParam String newPassword, @RequestParam(name = "token") String token) {
        return resetPasswordService.resetPasswordService(newPassword, token);
    }
}
