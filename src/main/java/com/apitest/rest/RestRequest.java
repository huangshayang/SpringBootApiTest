package com.apitest.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apitest.component.EnvComponent;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.util.ExceptionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author huangshayang
 */
@Log4j2
public class RestRequest {

    private static WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private static Map<String, Object> map;

    public static WebClient.RequestHeadersSpec<?> doGet(String domain, String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie, int envId) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = WebClient.create(domain).get().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, getCookie(envId));
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String baseUrl, String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie, int envId) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = WebClient.create(baseUrl).post().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(jsonData);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, getCookie(envId));
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPut(String baseUrl, String url, @Nullable String jsonData, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            return WebClient.create(baseUrl).put().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSION_KEY, "").syncBody(jsonData);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String baseUrl, String url, @Nullable String jsonData, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            return WebClient.create(baseUrl).delete().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE).cookie(ConfigConsts.SESSION_KEY, "");
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    private static String getCookie(int envId) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            map.putIfAbsent("username", EnvComponent.getUsername(envId));
            map.putIfAbsent("password", EnvComponent.getPassword(envId));
            Mono<ClientResponse> webClient = WebClient.create(EnvComponent.getDomain(envId))
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
