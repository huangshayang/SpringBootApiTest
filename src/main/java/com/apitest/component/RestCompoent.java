package com.apitest.component;

import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.mapper.LogMapper;
import com.apitest.rest.RestRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author huangshayang
 */
public class RestCompoent {

    private static WebClient.RequestHeadersSpec<?> body;
    private static int envId;
    private static RestCompoent restCompoent = new RestCompoent();

    @Resource
    private LogMapper logMapper;

    public static void taskApiCaseExecByLock(Apis apis, Cases cases) {
        //向外部发送http请求
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        ClientResponse response = restHttp(apis, cases).exchange().block();
        //把请求的结果保存到响应日志里
        responseWriteToLog(cases, response, requestTime);
    }

    /**
     * 响应结果存入Log表里
     *
     * @param cases
     * @param response
     * @param requestTime
     */
    private static void responseWriteToLog(Cases cases, ClientResponse response, Timestamp requestTime) {
        Logs logs = new Logs();
        logs.setRequestTime(requestTime);
        logs.setCode(Objects.requireNonNull(response).statusCode().value());
        logs.setResponseHeader(String.valueOf(Objects.requireNonNull(response).headers().asHttpHeaders()));
        logs.setResponseData(response.bodyToMono(String.class).block());
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
        String method = api.getMethod();
        String url = api.getUrl();
        String jsonData = cases.getJsonData();
        String paramsData = cases.getParamsData();
        envId = api.getEnvId();
        String baseUrl = EnvComponent.getEnviroment(envId).getDomain();
        boolean cookie = api.getCookie();
        switch (method) {
            case "get":
                body = RestRequest.doGet(baseUrl, url, jsonData, paramsData, cookie);
                break;
            case "post":
                body = RestRequest.doPost(baseUrl, url, jsonData, paramsData, cookie);
                break;
            case "put":
                body = RestRequest.doPut(baseUrl, url, jsonData, paramsData);
                break;
            case "delete":
                body = RestRequest.doDelete(baseUrl, url, paramsData);
                break;
            default:
        }
        return body;
    }

    public static int getEnvId() {
        return envId;
    }
}
