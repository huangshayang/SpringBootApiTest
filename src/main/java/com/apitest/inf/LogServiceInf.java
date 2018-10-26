package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Async
public interface LogServiceInf {
    CompletableFuture<Object> queryPageLogByApiIdService(HttpSession httpSession, int apiId, int page, int size);

    CompletableFuture<Object> deleteOneLogService(HttpSession httpSession, int id);

    CompletableFuture<Object> deleteAllLogByApiIdService(HttpSession httpSession, int apiId);
}
