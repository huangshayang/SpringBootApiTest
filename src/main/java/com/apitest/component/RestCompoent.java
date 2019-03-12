package com.apitest.component;

import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.mapper.LogMapper;
import com.apitest.rest.RestRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author huangshayang
 */
@Component
public class RestCompoent {

    private static int envId;
    private static RestCompoent restCompoent;

    @Resource
    private LogMapper logMapper;

    @PostConstruct
    public void init() {
        restCompoent = this;
        restCompoent.logMapper = this.logMapper;
    }

    public static void taskApiCaseExecByLock(Apis apis, Cases cases) {
        //向外部发送http请求
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        ClientResponse response = restHttp(apis, cases).exchange().block();
        responseWriteToLog(Objects.requireNonNull(response), requestTime, cases);
    }

    /**
     * 响应结果存入Log表里
     */
    private static void responseWriteToLog(ClientResponse response, Timestamp requestTime, Cases cases) {
        Logs logs = new Logs();
        logs.setRequestTime(requestTime);
        logs.setCode(response.statusCode().value());
        logs.setResponseHeader(String.valueOf(response.headers().asHttpHeaders()));
        logs.setResponseBody(response.bodyToMono(String.class).block());
        logs.setApiId(cases.getApiId());
        logs.setCaseName(cases.getName());
        restCompoent.logMapper.save(logs);
    }

    /**
     * 根据api的请求方法调用RestRequest类的对应请求方法
     *
     * @param api
     * @param cases
     * @return
     */
    private static WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        WebClient.RequestHeadersSpec<?> body = null;
        String method = api.getMethod();
        String url = api.getUrl();
        String jsonData = cases.getJsonData();
        String paramsData = cases.getParamsData();
        envId = api.getEnvId();
        boolean cookie = api.getCookie();
        switch (method) {
            case "get":
                body = RestRequest.doGet(url, jsonData, paramsData, cookie);
                break;
            case "post":
                body = RestRequest.doPost(url, jsonData, paramsData, cookie);
                break;
            case "put":
                body = RestRequest.doPut(url, jsonData, paramsData);
                break;
            case "delete":
                body = RestRequest.doDelete(url, paramsData);
                break;
            default:
        }
        return body;
    }

    public static int getEnvId() {
        return envId;
    }
}
