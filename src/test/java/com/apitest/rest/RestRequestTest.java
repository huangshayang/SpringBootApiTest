package com.apitest.rest;

import com.apitest.configconsts.ConfigConsts;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Log4j2
public class RestRequestTest {


    @Test
    public void doGet() throws IOException {
        String url = "http://localhost:8080";
        WebClient client = WebClient.create(url);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        requestHeadersSpec = client.get().uri("/api/all").cookie("uInfo", "$2a$10$517irWMF1zW0suhnpSHoXuoMlYflIWtq3AH8bE5/yKe9.fPIeeDa6").accept(MediaType.APPLICATION_JSON_UTF8);
        log.info(requestHeadersSpec.retrieve().bodyToMono(String.class).block());
//        System.out.println(RestRequest.doGet(url, "2", true).retrieve().bodyToMono(String.class).block());
//        String data = "{}";
//        boolean cookie = true;
//        String responseEntity = RestRequest.doGet(url, data, cookie).getBody();
//        Map map = JSON.parseObject(responseEntity, Map.class);
//        Assert.assertEquals(200, Objects.requireNonNull(map).get("status"));
    }

    @Test
    public void doPost() {
        String url = "http://localhost:8080";
        WebClient client = WebClient.create(url);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        String data = "{\"username\":\"admin\",\"password\":\"123456\"}";
        requestHeadersSpec = client.post().uri("/account/login").contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(data);
        System.out.println(requestHeadersSpec.exchange().block().cookies());
//        System.out.println(RestRequest.doPost(url, data, false).retrieve().bodyToMono(String.class).block());
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
}
