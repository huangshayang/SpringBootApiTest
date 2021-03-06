package com.apitest.controller;


import com.apitest.api.LogoutService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @DeleteMapping(value = "/logout")
    public ServerResponse logoutController(HttpServletRequest request) throws ServletException {
        return logoutService.logoutService(request);
    }

}
