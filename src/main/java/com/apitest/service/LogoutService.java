package com.apitest.service;


import com.apitest.error.ErrorEnum;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Async
public class LogoutService {

    public CompletableFuture<Object> logoutService(){
        Map<String, Object> map = new HashMap<>(8);
        map.put("status", ErrorEnum.LOGOUT_SUCCESS.getStatus());
        map.put("message", ErrorEnum.LOGOUT_SUCCESS.getMessage());
        return CompletableFuture.completedFuture(map);
    }

}
