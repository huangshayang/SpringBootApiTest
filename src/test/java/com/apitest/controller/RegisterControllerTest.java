package com.apitest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {
    private static final String DATA = "{\"username\":\"ABC\",\"password\":\"123456\"}";
    private static final String URL = "http://localhost:8080";
    private static final String URI = "/account/register";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,2, 2, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>(2));

    @Test
    void registerController1() {

    }

    void registe(){
        for (int i = 0; i < 2; i++) {
            threadPoolExecutor.execute(() -> WebClient.create(URL).post().uri(URI).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(DATA).retrieve().bodyToMono(Map.class).block());
        }
    }

    public static void main(String[] args) {
        RegisterControllerTest registerControllerTest = new RegisterControllerTest();
        registerControllerTest.registe();
    }
}