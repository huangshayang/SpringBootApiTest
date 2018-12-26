package com.apitest.component;

import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.repository.LogRepository;
import com.apitest.rest.RestRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huangshayang
 */
@Log4j2
@Component
public class RestCompoent {

    private ReentrantLock lock = new ReentrantLock();

    private final LogRepository logRepository;

    @Autowired
    public RestCompoent(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void taskApiCaseExecByLock(Apis apis, Cases cases) {
        //向外部发送http请求
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程进入");
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        lock.lock();
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",加锁成功");
        ClientResponse response = restHttp(apis, cases).exchange().block();
        lock.unlock();
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",释放锁完成");
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
    private void responseWriteToLog(Apis apis, Cases aCasesList, ClientResponse response, Timestamp requestTime) {
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
    private WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        String method = api.getMethod();
        String url =  api.getUrl();
        String jsonData = cases.getJsonData();
        String paramsData = cases.getParamsData();
        int envId = api.getEnvId();
        boolean cookie = api.getCookie();
        WebClient.RequestHeadersSpec<?> body = null;
        switch (method){
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
                body = RestRequest.doDelete(url, jsonData, paramsData);
                break;
            default:
        }
        return body;
    }
}
