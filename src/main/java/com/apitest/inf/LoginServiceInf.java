package com.apitest.inf;


import com.apitest.util.ServerResponse;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author huangshayang
 */
public interface LoginServiceInf {
    ServerResponse loginService(HttpServletResponse response, HttpSession httpSession, String username, String password);
}
