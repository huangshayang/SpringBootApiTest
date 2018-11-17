package com.apitest.inf;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Async
public interface MailSendServiceInf {
    CompletableFuture<Object> resetPasswordMailService(HttpServletRequest request, String email);

    CompletableFuture<Object> registerMailService(HttpServletRequest request, String email);
}
