package com.apitest.api;

import com.apitest.util.ServerResponse;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface LoginService {
    ServerResponse loginService(HttpServletResponse response, HttpSession httpSession, String username, String password);
}
