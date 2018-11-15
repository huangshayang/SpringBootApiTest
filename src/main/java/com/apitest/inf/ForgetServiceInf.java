package com.apitest.inf;


import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Async
public interface ForgetServiceInf {

    CompletableFuture<Object> resetPasswordService(String newPassword, String captcha);
}
