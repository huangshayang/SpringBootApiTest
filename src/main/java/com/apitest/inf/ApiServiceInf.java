package com.apitest.inf;

import com.apitest.entity.Apis;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Async
public interface ApiServiceInf {

    CompletableFuture<Object> addApiService(Apis api);

    CompletableFuture<Object> queryPageApiService(int page, int size);

    CompletableFuture<Object> queryOneApiService(int id);

    CompletableFuture<Object> modifyApiService(int id, Apis api);

    CompletableFuture<Object> deleteApiService(int id);

    CompletableFuture<Object> execApiService(int id);

    CompletableFuture<Object> execApiServiceOne(int id);
}
