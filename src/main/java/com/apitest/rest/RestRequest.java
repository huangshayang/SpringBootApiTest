package com.apitest.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.util.ExceptionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangshayang
 */
@Log4j2
public class RestRequest {

    private static final String COOKIE = getCookie();
    private static WebClient client = WebClient.create(ConfigConsts.URL);
    private static WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    public static WebClient.RequestHeadersSpec<?> doGet(String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            Map<String, Object> map = JSON.parseObject(jsonData, new TypeReference<>() {});
            requestHeadersSpec = client.get().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, COOKIE);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            requestHeadersSpec = client.post().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(jsonData);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, COOKIE);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPut(String url, @Nullable String jsonData, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            return client.put().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSION_KEY, COOKIE).syncBody(jsonData);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String url, @Nullable String jsonData, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            return client.delete().uri(url, jsonData).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE).cookie(ConfigConsts.SESSION_KEY, COOKIE);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    private static String getCookie() {
        try {
            String url = "http://alpha.smart-iov.net/api/v1/login";
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(8);
            map.putIfAbsent("username", ConfigConsts.USERNAME);
            map.putIfAbsent("password", ConfigConsts.PASSWORD);
            Mono<ClientResponse> webClient = WebClient.create(url)
                    .post()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .syncBody(map)
                    .exchange();
            return Objects.requireNonNull(Objects.requireNonNull(webClient.block()).cookies().getFirst(ConfigConsts.SESSION_KEY)).getValue();
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>(8);
//        map.put("id", 44);
//        map.put("label_ids", "12,121");
//        System.out.println(map);
//        String str = "{\"id\":44,\"label_ids\":\"12,121\"}";
//        //第一种方式
//        Map<String, Object> mapType = JSON.parseObject(str, new TypeReference<>() {});
//        System.out.println(mapType);
//        requestHeadersSpec = client.get().uri("/departments/{id}/vehicles?label_ids={label_ids}", mapType).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
//        requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, COOKIE);
//        String body = requestHeadersSpec.retrieve().bodyToMono(String.class).block();
//        System.out.println(body);
//    }
}
