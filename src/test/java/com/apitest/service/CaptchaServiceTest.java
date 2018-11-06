package com.apitest.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


class CaptchaServiceTest {

    @Test
    void captchaService() {
        String url = "http://localhost:8080";
        WebClient client = WebClient.create(url);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        requestHeadersSpec = client.post().uri("/account/verify").contentType(MediaType.APPLICATION_JSON_UTF8);
        System.out.println(requestHeadersSpec.retrieve().bodyToMono(String.class).block());
    }
}