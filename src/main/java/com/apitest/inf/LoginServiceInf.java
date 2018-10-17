package com.apitest.inf;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface LoginServiceInf {
    Object loginService(HttpSession httpSession, Map<String, String> models);
}
