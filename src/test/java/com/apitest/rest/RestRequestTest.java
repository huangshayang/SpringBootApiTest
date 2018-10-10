package com.apitest.rest;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


public class RestRequestTest {


    @Test
    public void doGet() throws IOException {
        String url = "/organizations/{id}/departments";
        System.out.println(RestRequest.doGet(url, "2", true).retrieve().bodyToMono(String.class).block());
//        String data = "{}";
//        boolean cookie = true;
//        String responseEntity = RestRequest.doGet(url, data, cookie).getBody();
//        Map map = JSON.parseObject(responseEntity, Map.class);
//        Assert.assertEquals(200, Objects.requireNonNull(map).get("status"));
    }

    @Test
    public void doPost() {

        String url = "/login";
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
