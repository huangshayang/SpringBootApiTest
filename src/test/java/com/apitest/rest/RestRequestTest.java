package com.apitest.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RestRequestTest {

    @Test
    public void doGet() {
        ClientResponse response = RestRequest.doGet("/get?name={name}", "{\"name\":\"123\"}", false).exchange().block();
        System.out.println(response.bodyToMono(String.class).block());
    }

    @Test
    public void doPost() {
        ClientResponse response = RestRequest.doPost("/post", "{\"name\":\"123\"}", null, false).exchange().block();
        System.out.println(response.bodyToMono(String.class).block());
    }

}