package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Async
public interface LoginServiceInf {
    CompletableFuture<Object> loginService(String username, String password);
}
