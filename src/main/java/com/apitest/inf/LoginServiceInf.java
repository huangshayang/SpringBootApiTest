package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Async
public interface LoginServiceInf {
    CompletableFuture<Object> loginService(HttpSession httpSession, Map<String, String> models);
}
