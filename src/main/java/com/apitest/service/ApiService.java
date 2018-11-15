package com.apitest.service;


import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ApiServiceInf;
import com.apitest.log.ExceptionLog;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import com.apitest.repository.LogRepository;
import com.apitest.rest.RestRequest;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Service
@Async
@Log4j2
public class ApiService implements ApiServiceInf {
    private final ApiRepository apiRepository;
    private final CaseRepository caseRepository;
    private final LogRepository logRepository;
    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    public ApiService(ApiRepository apiRepository, CaseRepository caseRepository, LogRepository logRepository) {
        this.apiRepository = apiRepository;
        this.caseRepository = caseRepository;
        this.logRepository = logRepository;
    }

    @Override
    public CompletableFuture<Object> addApiService(Apis api){
        Map<String, Object> map = new HashMap<>(8);
        log.info("参数: " + api);
        try {
            if (api.getUrl() == null || api.getMethod() == null || api.getCookie() == null || api.getNote() == null) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (api.getMethod().isEmpty()) {
                map.put("status", ErrorEnum.API_METHOD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
            }else if (api.getUrl().isEmpty()) {
                map.put("status", ErrorEnum.API_URL_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_URL_IS_EMPTY.getMessage());
            }else if (api.getCookie().toString().isEmpty()) {
                map.put("status", ErrorEnum.API_COOKIE_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
            }else if (api.getNote().isEmpty()) {
                map.put("status", ErrorEnum.API_NOTE_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_NOTE_IS_EMPTY.getMessage());
            }else if (apiRepository.existsByUrlAndMethod(api.getUrl(), api.getMethod())) {
                map.put("status", ErrorEnum.API_IS_REPEAT.getStatus());
                map.put("message", ErrorEnum.API_IS_REPEAT.getMessage());
            }else {
                apiRepository.save(api);
                map.put("status", ErrorEnum.API_ADD_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_ADD_SUCCESS.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            new ExceptionLog(e, api);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> queryPageApiService(int page, int size){
        Map<String, Object> map = new HashMap<>(8);
        try {
            if (page <0 || size <= 0) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else {
                Sort sort = new Sort(Sort.Direction.ASC, "id");
                Page<Apis> apis = apiRepository.findAll(PageRequest.of(page, size, sort));
                map.put("data", apis);
                map.put("status", ErrorEnum.API_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_QUERY_SUCCESS.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, page, size);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> queryOneApiService(int id){
        Map<String, Object> map = new HashMap<>(8);
        try {
            if (apiRepository.findById(id).isPresent()) {
                Optional<Apis> api = apiRepository.findById(id);
                map.put("data", api);
                map.put("status", ErrorEnum.API_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_QUERY_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, page, size);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> modifyApiService(int id, Apis api){
        Map<String, Object> map = new HashMap<>(8);
        log.info("参数: " + api);
        try {
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                apis.setUrl(api.getUrl());
                apis.setUpdateTime(new Date(System.currentTimeMillis()));
                apis.setMethod(api.getMethod());
                apis.setNote(api.getNote());
                apis.setCookie(api.getCookie());
                apiRepository.saveAndFlush(apis);
                map.put("status", ErrorEnum.API_MODIFY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_MODIFY_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, page, size);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> deleteApiService(int id){
        Map<String, Object> map = new HashMap<>(8);
        try {
            if (apiRepository.findById(id).isPresent()) {
                logRepository.deleteByApiId(id);
                caseRepository.deleteByApiId(id);
                apiRepository.deleteById(id);
                map.put("status", ErrorEnum.API_DELETE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_DELETE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, page, size);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> execApiService(int id) {
        Map<String, Object> map = new HashMap<>(8);
        try {
            List<Cases> casesList = caseRepository.findByApiId(id);
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                //根据case的数量起对应数量的多线程
                casesList.forEach(cases -> new Thread(() -> apicase(apis, cases)).start());
                map.put("status", ErrorEnum.HTTP_EXEC_SUCCESS.getStatus());
                map.put("message", ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> execApiServiceOne(int id){
        Map<String, Object> map = new HashMap<>(8);
        try {
            List<Cases> casesList = caseRepository.findByApiId(id);
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                for (Cases aCasesList : casesList) {
                    apicase(apis, aCasesList);
                }
                map.put("status", ErrorEnum.HTTP_EXEC_SUCCESS.getStatus());
                map.put("message", ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(map);
    }

    private void apicase(Apis apis, Cases aCasesList) {
        //向外部发送http请求
        lock.lock();
        ClientResponse response = restHttp(apis, aCasesList).exchange().block();
        //把请求的结果保存到响应日志里
        Logs logs = new Logs();
        logs.setRequestData(aCasesList.getRequestData());
        logs.setRequestTime(new Timestamp(System.currentTimeMillis()));
        logs.setCode(Objects.requireNonNull(response).statusCode().value());
        logs.setResponseHeader(String.valueOf(Objects.requireNonNull(response).headers().asHttpHeaders()));
        logs.setResponseData(response.bodyToMono(String.class).block());
        logs.setApiId(apis.getId());
        logs.setNote(aCasesList.getNote());
        logRepository.save(logs);
        lock.unlock();
    }

    private WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        String method = api.getMethod();
        String url =  api.getUrl();
        String data = cases.getRequestData();
        boolean cookie = api.getCookie();
        WebClient.RequestHeadersSpec<?> body = null;
        switch (method){
            case "get":
                body = RestRequest.doGet(url, data, cookie);
                break;
            case "post":
                body = RestRequest.doPost(url, data, cookie);
                break;
            case "put":
                body = RestRequest.doPut(url, data);
                break;
            case "delete":
                body = RestRequest.doDelete(url, data);
                break;
            default:
        }
        return body;
    }
}
