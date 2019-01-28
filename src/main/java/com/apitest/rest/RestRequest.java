package com.apitest.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apitest.component.EnvComponent;
import com.apitest.component.RestCompoent;
import com.apitest.configconsts.ConfigConsts;
import com.apitest.entity.Enviroment;
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
    private static WebClient.Builder webClient = WebClient.builder();
    private static String cookies;

    static {
        int envId = RestCompoent.getEnvId();
        cookies = getCookie(envId);
    }

    public static WebClient.RequestHeadersSpec<?> doGet(String baseUrl, String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = webClient.baseUrl(baseUrl).build().get().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, cookies);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String baseUrl, String url, @Nullable String jsonData, @Nullable String paramsData, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("jsonData: " + jsonData);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            requestHeadersSpec = webClient.baseUrl(baseUrl).build().post().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(jsonData);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, cookies);
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
            return webClient.baseUrl(baseUrl).build().put().uri(url, map).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSION_KEY, cookies).syncBody(jsonData);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String baseUrl, String url, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("paramsData: " + paramsData);
            try {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }catch (Exception e){
                map = new HashMap<>(1);
            }
            return webClient.baseUrl(baseUrl).build().delete().uri(url, map).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE).cookie(ConfigConsts.SESSION_KEY, cookies);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    private static String getCookie(int envId) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            Enviroment enviroment = EnvComponent.getEnviroment(envId);
            map.putIfAbsent("username", enviroment.getUsername());
            map.putIfAbsent("password", enviroment.getPassword());
            Mono<ClientResponse> clientResponseMono = webClient.baseUrl(enviroment.getDomain()).build()
                    .post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .syncBody(map)
                    .exchange();
            return Objects.requireNonNull(Objects.requireNonNull(clientResponseMono.block()).cookies().getFirst(ConfigConsts.SESSION_KEY)).getValue();
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }
}
