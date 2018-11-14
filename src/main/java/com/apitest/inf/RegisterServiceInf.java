package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Async
public interface RegisterServiceInf {
    CompletableFuture<Object> registerService(String username, String password, String captcha);

//    CompletableFuture<Object> registerMailService(String email);
}
