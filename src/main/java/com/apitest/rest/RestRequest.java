package com.apitest.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apitest.component.EnvComponent;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.util.ExceptionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author huangshayang
 */
@Log4j2
@Component
public class RestRequest {

//    private static final String COOKIE = getCookie();
    private static WebClient client = WebClient.create(ConfigConsts.URL);
    private static WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private static Map<String, Object> map;

    private static EnvComponent envComponent;

    @Autowired
    public RestRequest(EnvComponent envComponent){
        RestRequest.envComponent = envComponent;
    }

    public static WebClient.RequestHeadersSpec<?> doGet(String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie, int envId) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = WebClient.create(envComponent.getDomain(envId)).get().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, getCookie(envId));
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie, int envId) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = client.post().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(jsonData);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, getCookie(envId));
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
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            return client.put().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSION_KEY, "").syncBody(jsonData);
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
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            return client.delete().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE).cookie(ConfigConsts.SESSION_KEY, "");
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    private static String getCookie(int envId) {
        try {
            String url = "http://alpha.smart-iov.net/api/v1/login";
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(8);
            map.putIfAbsent("username", envComponent.getUsername(envId));
            map.putIfAbsent("password", envComponent.getPassword(envId));
            Mono<ClientResponse> webClient = WebClient.create(envComponent.getDomain(envId))
                    .post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .syncBody(map)
                    .exchange();
            return Objects.requireNonNull(Objects.requireNonNull(webClient.block()).cookies().getFirst(ConfigConsts.SESSION_KEY)).getValue();
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }
}
