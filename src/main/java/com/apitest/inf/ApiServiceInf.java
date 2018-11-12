package com.apitest.inf;

import com.apitest.entity.Apis;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Async
public interface ApiServiceInf {

    CompletableFuture<Object> addApiService(HttpServletRequest request, Apis api);

    CompletableFuture<Object> queryPageApiService(HttpServletRequest request, int page, int size);

    CompletableFuture<Object> queryOneApiService(HttpServletRequest request, int id);

    CompletableFuture<Object> modifyApiService(HttpServletRequest request, int id, Apis api);

    CompletableFuture<Object> deleteApiService(HttpServletRequest request, int id);

    CompletableFuture<Object> execApiService(HttpSession httpSession, int id);

    CompletableFuture<Object> execApiServiceOne(HttpSession httpSession, int id);
}
