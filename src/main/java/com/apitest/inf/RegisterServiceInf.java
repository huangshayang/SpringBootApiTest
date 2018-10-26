package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Async
public interface RegisterServiceInf {
    CompletableFuture<Object> registerService(HttpSession httpSession, Map<String, String> models);
}
