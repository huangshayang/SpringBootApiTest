package com.apitest.inf;


import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.Callable;

public interface ForgetServiceInf {

    Callable<Object> forgetPasswordService(HttpSession httpSession, Map<String, Object> models);

    Callable<Object> getTokenService(HttpSession httpSession, Map<String, Object> username);
}
