package com.apitest.controller;


import com.apitest.service.RegisterService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


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
    public ServerResponse registerController(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "token") String token){
        return registerService.registerService(username, password, token);
    }
}
