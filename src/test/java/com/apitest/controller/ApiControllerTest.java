package com.apitest.controller;

import com.apitest.rest.RestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ApiControllerTest {

    private static final String URL = "http://localhost:8080";
    private static final String LOGIN_URI = "/account/login";
    private static final String API_ADD_URI = "/api/add";
    private static final String DATA = "{\"url\":\"/login\",\"method\":\"post\",\"is_cookie\":\"false\",\"note\":\"登录\"}";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,2, 2, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>(2));


    void addApiController(String user) {

        for (int i = 0; i < 2; i++) {
            threadPoolExecutor.execute(() -> WebClient.create(URL).post().uri(API_ADD_URI).contentType(MediaType.APPLICATION_JSON_UTF8).cookie("SESSION",getCookie(user)).syncBody(DATA).retrieve().bodyToMono(Map.class).block());
        }
    }

    @Test
    void modifyApiController() {
    }

    @Test
    void deleteApiController() {
    }

    static String getCookie(String user){
        Mono<ClientResponse> webClient = WebClient.create(URL)
                .post()
                .uri(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(user)
                .exchange();
        return Objects.requireNonNull(Objects.requireNonNull(webClient.block()).cookies().getFirst("SESSION")).getValue();
    }

    public static void main(String[] args) {
//        String user1 = "{\"username\":\"shayang888\",\"password\":\"123456\"}";
//        String user2 = "{\"username\":\"shayang999\",\"password\":\"123456\"}";
//        ApiControllerTest apiControllerTest = new ApiControllerTest();
//        apiControllerTest.addApiController(user1);

    }
}