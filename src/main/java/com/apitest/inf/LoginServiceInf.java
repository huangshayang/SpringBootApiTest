package com.apitest.inf;

import com.apitest.entity.User;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Async
public interface LoginServiceInf {
    CompletableFuture<Object> loginService(HttpServletResponse response, HttpSession httpSession, User user);
}
