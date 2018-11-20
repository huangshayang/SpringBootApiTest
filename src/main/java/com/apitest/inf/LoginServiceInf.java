package com.apitest.inf;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author huangshayang
 */
public interface LoginServiceInf {
    Object loginService(HttpServletResponse response, HttpSession httpSession, String username, String password);
}
