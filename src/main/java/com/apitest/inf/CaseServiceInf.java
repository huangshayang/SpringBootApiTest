package com.apitest.inf;

import com.apitest.entity.Cases;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Async
public interface CaseServiceInf {
    CompletableFuture<Object> queryCaseByApiIdService(HttpSession httpSession, int apiId);

    CompletableFuture<Object> queryOneCaseService(HttpSession httpSession, int id);

    CompletableFuture<Object> deleteAllCaseByApiIdService(HttpSession httpSession, int apiId);

    CompletableFuture<Object> modifyCaseService(HttpSession httpSession, int id, Cases cases);

    CompletableFuture<Object> deleteOneCaseService(HttpSession httpSession, int id);

    CompletableFuture<Object> addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId);
}
