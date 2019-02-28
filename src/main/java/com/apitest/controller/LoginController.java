package com.apitest.controller;

import com.apitest.inf.LoginServiceInf;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class LoginController {
    private LoginServiceInf loginServiceInf;

    @Autowired
    public LoginController(LoginServiceInf loginServiceInf) {
        this.loginServiceInf = loginServiceInf;
    }

    @PostMapping(value = "/login")
    public ServerResponse loginController(HttpServletResponse response, HttpSession httpSession, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        return loginServiceInf.loginService(response, httpSession, username, password);
    }
}
