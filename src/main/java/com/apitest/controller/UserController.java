package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.service.UserService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user/profile", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ServerResponse userInfoController(HttpServletRequest request){
        return userService.userInfoService(request);
    }
}
