package com.apitest.rest;


import com.apitest.configconsts.ConfigConsts;
import com.apitest.util.ExceptionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

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

    public static WebClient.RequestHeadersSpec<?> doGet(String url, @Nullable String data, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("data:" + data);
            requestHeadersSpec = client.get().uri(url, data).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, COOKIE);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String url, @Nullable String data, boolean cookie) {
        try {
            log.info("uri: " + url);
            log.info("data:" + data);
            requestHeadersSpec = client.post().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(data);
            if (cookie) {
                requestHeadersSpec.cookie(ConfigConsts.SESSION_KEY, COOKIE);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPut(String url, String data) {
        try {
            log.info("uri: " + url);
            log.info("data:" + data);
            return client.put().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSION_KEY, COOKIE).syncBody(data);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String url, String data) {
        try {
            log.info("uri: " + url);
            log.info("data:" + data);
            return client.delete().uri(url, data).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE).cookie(ConfigConsts.SESSION_KEY, COOKIE);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
    }

    private static String getCookie() {
        try {
            String url = "http://alpha.smart-iov.net/api/v1/login";
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(8);
            map.put("username", ConfigConsts.USERNAME);
            map.put("password", ConfigConsts.PASSWORD);
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
}
