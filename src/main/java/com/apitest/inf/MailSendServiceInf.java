package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Async
public interface MailSendServiceInf {
    CompletableFuture<Object> resetPasswordMailService(String email);

    CompletableFuture<Object> registerMailService(String email);
}
