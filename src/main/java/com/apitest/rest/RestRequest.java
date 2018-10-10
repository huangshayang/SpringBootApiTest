package com.apitest.rest;


import com.apitest.configconsts.ConfigConsts;
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
public class RestRequest {

    private static final String COOKIES = getCookie();
    private static WebClient client = WebClient.create(ConfigConsts.URL);
    private static WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    public static WebClient.RequestHeadersSpec<?> doGet(String url, @Nullable String data, boolean isCookie) {
        requestHeadersSpec = client.get().uri(url, data).accept(MediaType.APPLICATION_JSON_UTF8);
        if (isCookie) {
            requestHeadersSpec.cookie(ConfigConsts.SESSIONKEY, COOKIES);
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPost(String url, @Nullable String data, boolean isCookie) {
        requestHeadersSpec = client.post().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(data);
        if (isCookie) {
            requestHeadersSpec.cookie(ConfigConsts.SESSIONKEY, COOKIES);
        }
        return requestHeadersSpec;
    }

    public static WebClient.RequestHeadersSpec<?> doPut(String url, String data) {
        return client.put().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSIONKEY, COOKIES).syncBody(data);
    }

    public static WebClient.RequestHeadersSpec<?> doDelete(String url, String data) {
        return client.delete().uri(url, data).accept(MediaType.APPLICATION_JSON_UTF8).cookie(ConfigConsts.SESSIONKEY, COOKIES);
    }

    private static String getCookie() {
        final String url = "http://alpha.smart-iov.net/api/v1/login";
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(8);
        map.put("username", ConfigConsts.USERNAME);
        map.put("password", ConfigConsts.PASSWORD);
        Mono<ClientResponse> webClient = WebClient.create(url)
                .post()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(map)
                .exchange();
        return Objects.requireNonNull(Objects.requireNonNull(webClient.block()).cookies().getFirst(ConfigConsts.SESSIONKEY)).getValue();
    }
}
