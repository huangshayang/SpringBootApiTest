package com.apitest.inf;


import javax.servlet.http.HttpSession;
import java.util.Map;

public interface ForgetServiceInf {

    Object forgetPasswordService(HttpSession httpSession, Map<String, String> models);

    Object getTokenService(HttpSession httpSession);
}
