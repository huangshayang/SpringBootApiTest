package com.apitest.service;


import com.apitest.error.ErrorEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class LogoutService {

    public Callable<Object> logoutService(HttpSession httpSession){
        Map<String, Object> map = new HashMap<>(8);
        httpSession.invalidate();
        map.put("status", ErrorEnum.LOGOUT_SUCCESS.getStatus());
        map.put("message", ErrorEnum.LOGOUT_SUCCESS.getMessage());
        return () -> map;
    }

}
