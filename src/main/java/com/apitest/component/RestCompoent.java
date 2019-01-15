package com.apitest.component;

import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.repository.LogRepository;
import com.apitest.rest.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author huangshayang
 */
@Component
public class RestCompoent {

    private static LogRepository logRepository;
    private static WebClient.RequestHeadersSpec<?> body;

    @Autowired
    public RestCompoent(LogRepository logRepository) {
        RestCompoent.logRepository = logRepository;
    }

    public static void taskApiCaseExecByLock(Apis apis, Cases cases) {
        //向外部发送http请求
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        ClientResponse response = restHttp(apis, cases).exchange().block();
        //把请求的结果保存到响应日志里
        responseWriteToLog(apis, cases, response, requestTime);
    }

    /**
     * 响应结果存入Log表里
     * @param apis
     * @param aCasesList
     * @param response
     * @param requestTime
     */
    private static void responseWriteToLog(Apis apis, Cases aCasesList, ClientResponse response, Timestamp requestTime) {
        Logs logs = new Logs();
        logs.setJsonData(aCasesList.getJsonData());
        logs.setParamsData(aCasesList.getParamsData());
        logs.setRequestTime(requestTime);
        logs.setCode(Objects.requireNonNull(response).statusCode().value());
        logs.setResponseHeader(String.valueOf(Objects.requireNonNull(response).headers().asHttpHeaders()));
        logs.setResponseData(response.bodyToMono(String.class).block());
        logs.setApiId(apis.getId());
        logs.setCaseName(aCasesList.getName());
        logRepository.save(logs);
    }

    /**
     * 根据api的请求方法调用RestRequest类的对应请求方法
     * @param api
     * @param cases
     * @return
     */
    private static WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        String method = api.getMethod();
        String url =  api.getUrl();
        String jsonData = cases.getJsonData();
        String paramsData = cases.getParamsData();
        int envId = api.getEnvId();
        String baseUrl = EnvComponent.getEnviroment(envId).getDomain();
        boolean cookie = api.getCookie();
        switch (method){
            case "get":
                body = RestRequest.doGet(baseUrl, url, jsonData, paramsData, cookie, envId);
                break;
            case "post":
                body = RestRequest.doPost(baseUrl, url, jsonData, paramsData, cookie, envId);
                break;
            case "put":
                body = RestRequest.doPut(baseUrl, url, jsonData, paramsData, envId);
                break;
            case "delete":
                body = RestRequest.doDelete(baseUrl, url, jsonData, paramsData, envId);
                break;
            default:
        }
        return body;
    }
}
