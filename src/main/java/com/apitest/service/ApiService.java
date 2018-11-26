package com.apitest.service;


import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ApiServiceInf;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import com.apitest.repository.LogRepository;
import com.apitest.rest.RestRequest;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class ApiService implements ApiServiceInf {
    private final ApiRepository apiRepository;
    private final CaseRepository caseRepository;
    private final LogRepository logRepository;
    private ReentrantLock lock = new ReentrantLock();
    private static ServerResponse serverResponse;

    @Autowired
    public ApiService(ApiRepository apiRepository, CaseRepository caseRepository, LogRepository logRepository) {
        this.apiRepository = apiRepository;
        this.caseRepository = caseRepository;
        this.logRepository = logRepository;
    }

    @Override
    public ServerResponse addApiService(Apis api){
        log.info("参数: " + api);
        try {
            if (api.getMethod().isBlank()) {
                serverResponse = new ServerResponse(ErrorEnum.API_METHOD_IS_EMPTY.getStatus(), ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
            }else if (api.getUrl().isBlank()) {
                serverResponse = new ServerResponse(ErrorEnum.API_URL_IS_EMPTY.getStatus(), ErrorEnum.API_URL_IS_EMPTY.getMessage());
            }else if (api.getCookie().toString().isBlank()) {
                serverResponse = new ServerResponse(ErrorEnum.API_COOKIE_IS_EMPTY.getStatus(), ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
            }else if (api.getNote().isBlank()) {
                serverResponse = new ServerResponse(ErrorEnum.API_NOTE_IS_EMPTY.getStatus(), ErrorEnum.API_NOTE_IS_EMPTY.getMessage());
            }else if (apiRepository.existsByUrlAndMethod(api.getUrl(), api.getMethod())) {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_REPEAT.getStatus(), ErrorEnum.API_IS_REPEAT.getMessage());
            }else {
                apiRepository.save(api);
                serverResponse = new ServerResponse(ErrorEnum.API_ADD_SUCCESS.getStatus(), ErrorEnum.API_ADD_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryPageApiService(int page, int size){
        try {
            if (page <0 || size <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            }else {
                Sort sort = new Sort(Sort.Direction.ASC, "id");
                Page<Apis> apis = apiRepository.findAll(PageRequest.of(page, size, sort));
                serverResponse = new ServerResponse<>(ErrorEnum.API_QUERY_SUCCESS.getStatus(), ErrorEnum.API_QUERY_SUCCESS.getMessage(), apis);
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryOneApiService(int id){
        try {
            Optional<Apis> api = apiRepository.findById(id);
            serverResponse = new ServerResponse<>(ErrorEnum.API_QUERY_SUCCESS.getStatus(), ErrorEnum.API_QUERY_SUCCESS.getMessage(), api);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse modifyApiService(int id, Apis api){
        log.info("参数: " + api);
        try {
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                if (api.getUrl().isBlank()) {
                    serverResponse = new ServerResponse(ErrorEnum.API_URL_IS_EMPTY.getStatus(), ErrorEnum.API_URL_IS_EMPTY.getMessage());
                }else if (api.getMethod().isBlank()) {
                    serverResponse = new ServerResponse(ErrorEnum.API_METHOD_IS_EMPTY.getStatus(), ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
                }else if (api.getCookie().toString().isBlank()) {
                    serverResponse = new ServerResponse(ErrorEnum.API_COOKIE_IS_EMPTY.getStatus(), ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
                }else if (api.getNote().isBlank()) {
                    serverResponse = new ServerResponse(ErrorEnum.API_NOTE_IS_EMPTY.getStatus(), ErrorEnum.API_NOTE_IS_EMPTY.getMessage());
                }else if (apiRepository.existsByUrlAndMethod(api.getUrl(), api.getMethod())) {
                    serverResponse = new ServerResponse(ErrorEnum.API_IS_REPEAT.getStatus(), ErrorEnum.API_IS_REPEAT.getMessage());
                }else {
                    apis.setUrl(api.getUrl());
                    apis.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    apis.setMethod(api.getMethod());
                    apis.setNote(api.getNote());
                    apis.setCookie(api.getCookie());
                    apiRepository.saveAndFlush(apis);
                    serverResponse = new ServerResponse(ErrorEnum.API_MODIFY_SUCCESS.getStatus(), ErrorEnum.API_MODIFY_SUCCESS.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteApiService(int id){
        try {
            if (apiRepository.findById(id).isPresent()) {
                logRepository.deleteByApiId(id);
                caseRepository.deleteByApiId(id);
                apiRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.API_DELETE_SUCCESS.getStatus(), ErrorEnum.API_DELETE_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse execApiService(int id) {
        try {
            if (apiRepository.findById(id).isPresent()) {
                List<Cases> casesList = caseRepository.findByApiId(id);
                Apis apis = apiRepository.findById(id).get();
                //根据case的数量起对应数量的多线程
                casesList.forEach(cases -> new Thread(() -> apiCaseExecByLock(apis, cases)).start());
                serverResponse = new ServerResponse(ErrorEnum.HTTP_EXEC_SUCCESS.getStatus(), ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse execApiServiceOne(int id){
        try {
            if (apiRepository.findById(id).isPresent()) {
                List<Cases> casesList = caseRepository.findByApiId(id);
                Apis apis = apiRepository.findById(id).get();
                for (Cases aCasesList : casesList) {
                    apiCaseExec(apis, aCasesList);
                }
                serverResponse = new ServerResponse(ErrorEnum.HTTP_EXEC_SUCCESS.getStatus(), ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    /**
     * 多线程并发执行测试用例，为了避免用例重复使用，所以加入ReentrantLock的非公平锁
     * @param apis
     * @param aCasesList
     */
    private void apiCaseExecByLock(Apis apis, Cases aCasesList) {
        //向外部发送http请求
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程进入");
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        lock.lock();
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",加锁成功");
        ClientResponse response = restHttp(apis, aCasesList).exchange().block();
        lock.unlock();
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",释放锁完成");
        //把请求的结果保存到响应日志里
        responseWriteToLog(apis, aCasesList, response, requestTime);
    }

    /**
     * 单个线程执行所有用例
     * @param apis
     * @param aCasesList
     */
    private void apiCaseExec(Apis apis, Cases aCasesList) {
        //向外部发送http请求
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        ClientResponse response = restHttp(apis, aCasesList).exchange().block();
        //把请求的结果保存到响应日志里
        responseWriteToLog(apis, aCasesList, response, requestTime);
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
        logs.setRequestData(aCasesList.getRequestData());
        logs.setRequestTime(requestTime);
        logs.setCode(Objects.requireNonNull(response).statusCode().value());
        logs.setResponseHeader(String.valueOf(Objects.requireNonNull(response).headers().asHttpHeaders()));
        logs.setResponseData(response.bodyToMono(String.class).block());
        logs.setApiId(apis.getId());
        logs.setNote(aCasesList.getNote());
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
