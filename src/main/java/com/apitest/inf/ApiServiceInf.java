package com.apitest.inf;

import com.apitest.entity.Apis;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Async
public interface ApiServiceInf {

    CompletableFuture<Object> addApiService(HttpSession httpSession, Apis api);

    CompletableFuture<Object> queryPageApiService(HttpSession httpSession, int page, int size);

    CompletableFuture<Object> queryOneApiService(HttpSession httpSession, int id);

    CompletableFuture<Object> modifyApiService(HttpSession httpSession, int id, Apis api);

    CompletableFuture<Object> deleteApiService(HttpSession httpSession, int id);

    CompletableFuture<Object> execApiService(HttpSession httpSession, int id);

    CompletableFuture<Object> execApiServiceOne(HttpSession httpSession, int id);
}
