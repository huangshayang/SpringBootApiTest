package com.apitest.inf;


import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Async
public interface ForgetServiceInf {

    CompletableFuture<Object> forgetPasswordService(HttpSession httpSession, Map<String, String> models);

    CompletableFuture<Object> getTokenService(HttpSession httpSession);
}
