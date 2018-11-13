package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Async
public interface LoginServiceInf {
    CompletableFuture<Object> loginService(HttpServletResponse response, String username, String password);
}
