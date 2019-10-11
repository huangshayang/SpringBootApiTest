package com.apitest.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apitest.component.EnvComponent;
import com.apitest.component.RestCompoent;
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
    private static WebClient client = WebClient.create("http://localhost:8888");
    private static String cookies;

    static {
        int envId = RestCompoent.getEnvId();
//        client = WebClient.create(EnvComponent.getEnviroment(envId).getDomain());
        cookies = getCookie(envId);
    }

    public static WebClient.RequestHeadersSpec<?> doGet(String url, @Nullable String paramsData, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("paramsData: " + paramsData);
            requestHeadersSpec = client.get().uri(url, revert(paramsData)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            if (cookie) {
                requestHeadersSpec.header(HttpHeaders.COOKIE, cookies);
            }
        } catch (Exception e) {
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
            requestHeadersSpec = client.post().uri(url, revert(paramsData)).contentType(MediaType.APPLICATION_JSON).bodyValue(jsonData);
            if (cookie) {
                requestHeadersSpec.header(HttpHeaders.COOKIE, cookies);
            }
        } catch (Exception e) {
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
            requestHeadersSpec = client.put().uri(url, revert(paramsData)).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.COOKIE, cookies).bodyValue(jsonData);
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String url, @Nullable String paramsData) {
        try {
            log.info("uri: " + url);
            log.info("paramsData: " + paramsData);
            return client.get().uri(url, revert(paramsData)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.COOKIE, cookies);
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
    }

    private static Map<String, Object> revert(@Nullable String paramsData) {
        log.info("paramsData: " + paramsData);
        Map<String, Object> map;
        try {
            if (paramsData == null) {
                map = new HashMap<>(0);
            }else {
                map = JSON.parseObject(paramsData, new TypeReference<>() {});
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("map: " + map);
        return map;
    }

    private static String getCookie(int envId) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            Enviroment enviroment = EnvComponent.getEnviroment(envId);
            map.putIfAbsent("username", enviroment.getUsername());
            map.putIfAbsent("password", enviroment.getPassword());
            Mono<ClientResponse> clientResponseMono = client
                    .post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map, Map.class)
                    .exchange();
            return Objects.requireNonNull(clientResponseMono.block()).cookies().toSingleValueMap().values().iterator().next().toString();
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
    }

}
