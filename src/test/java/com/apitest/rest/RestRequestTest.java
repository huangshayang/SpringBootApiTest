package com.apitest.rest;

import com.apitest.configconsts.ConfigConsts;
import com.apitest.error.ErrorEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
public class RestRequestTest {
    private static String url = "http://localhost:8080";
    private static WebClient client = WebClient.create(url);

    @Test
    public void doGet() throws IOException {
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = client.get().uri("/api/all").accept(MediaType.APPLICATION_JSON_UTF8).header("auth", "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJjYTBiZTI5NC02M2Y1LTQ0NTgtYjA0MC1lNGI4YzI0MzAxNjAiLCJpYXQiOjE1NDE2NzM3NTksImV4cCI6MTU0MTY3NTU1OSwic3ViIjoiVXNlcihpZD0xLCB1c2VybmFtZT1hZG1pbiwgcGFzc3dvcmQ9JDJhJDEwJGJBaFpYR0JDOG90ZmxsREduY0M1ZmVyMnpjcVp6SllCQXNlVERMeEtnbmFnVi5yc0lsSGhXKSJ9.XPEzolQmr8NrK7qTZo-jOYts_NJyE0MMnLlOms-X3w4s5Exy8KyHDRKhyRFhlc_3nynFxq9Ku-fc6KWWyyhjig");
        log.info(requestHeadersSpec.retrieve().bodyToMono(String.class).block());
//        System.out.println(RestRequest.doGet(url, "2", true).retrieve().bodyToMono(String.class).block());
//        String data = "{}";
//        boolean cookie = true;
//        String responseEntity = RestRequest.doGet(url, data, cookie).getBody();
//        Map map = JSON.parseObject(responseEntity, Map.class);
//        Assert.assertEquals(200, Objects.requireNonNull(map).get("status"));
    }


    @Test
    public void getHeader() {
        String url = "http://localhost:8080/account/login";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "admin");
        map.add("password", "123456");
        Mono<ClientResponse> webClient = WebClient.create(url)
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .syncBody(map)
                .exchange();
        System.out.println(webClient.block().headers().asHttpHeaders());
    }

    @Test
    public void doPut() {
        String url = "http://localhost:8080";
        WebClient client = WebClient.create(url);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        requestHeadersSpec = client.post().uri("/account/verify").contentType(MediaType.APPLICATION_JSON_UTF8);
        System.out.println(requestHeadersSpec.retrieve().bodyToMono(String.class).block());
    }

    @Test
    public void doDelete() {
    }

    @Test
    public void strBlankTest() {
        String str = " ";
        System.out.println(str.isEmpty());
    }

    public static void main(String[] args) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>(8);
        map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
        map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        ObjectMapper jsonObject = new ObjectMapper();
        String json = jsonObject.writeValueAsString(map);
        System.out.println(json);
    }
}
