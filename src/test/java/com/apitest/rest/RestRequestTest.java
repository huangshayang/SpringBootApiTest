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
        String url = "http://alpha.smart-iov.net";
        WebClient client = WebClient.create(url);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        requestHeadersSpec = client.get().uri("/profile").cookie("IOV_SESSION", "cacdba96c95044dc9350f2b04325328d").accept(MediaType.APPLICATION_JSON_UTF8);
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

        String url = "/profile";
        String data = "{\"username\":\"16602811927\",\"password\":\"d7190eb194ff9494625514b6d178c87f99c5973e28c398969d2233f2960a573e\"}";
        System.out.println(RestRequest.doPost(url, data, false).retrieve().bodyToMono(String.class).block());
    }

    @Test
    public void doPut() {
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
