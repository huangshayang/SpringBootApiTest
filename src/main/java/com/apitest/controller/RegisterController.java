package com.apitest.controller;


import com.apitest.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    private RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/register")
    public Object registerController(HttpSession httpSession, @RequestBody Map<String, String> models){
        return registerService.registerService(httpSession, models);
    }
}
