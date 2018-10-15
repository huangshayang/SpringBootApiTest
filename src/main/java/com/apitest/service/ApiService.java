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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ApiService implements ApiServiceInf {
    private final ApiRepository apiRepository;
    private final CaseRepository caseRepository;
    private final LogRepository logRepository;
    private static ReentrantLock lock = new ReentrantLock();

    @Autowired
    private ApiService(ApiRepository apiRepository, CaseRepository caseRepository, LogRepository logRepository) {
        this.apiRepository = apiRepository;
        this.caseRepository = caseRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Callable<Object> addApiService(HttpSession httpSession, Apis api){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (api.getUrl() == null || api.getMethod() == null) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (api.getMethod().isEmpty()) {
                map.put("status", ErrorEnum.API_METHOD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
            }else if (api.getUrl().isEmpty()) {
                map.put("status", ErrorEnum.API_URL_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_URL_IS_EMPTY.getMessage());
            }else if (api.getIs_cookie() == null) {
                map.put("status", ErrorEnum.API_COOKIE_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
            }else if (apiRepository.existsByUrlAndMethod(api.getUrl(), api.getMethod())) {
                map.put("status", ErrorEnum.API_IS_REPEAT.getStatus());
                map.put("message", ErrorEnum.API_IS_REPEAT.getMessage());
            }else {
                setApiTimeDefault(api);
                apiRepository.save(api);
                map.put("status", ErrorEnum.API_ADD_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_ADD_SUCCESS.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> queryPageApiService(HttpSession httpSession, int page, int size){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (page <0 || size < 0) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else {
                Sort sort = new Sort(Sort.Direction.ASC, "id");
                Page<Apis> apis = apiRepository.findAll(PageRequest.of(page, size, sort));
                map.put("data", apis);
                map.put("status", ErrorEnum.API_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_QUERY_SUCCESS.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> queryOneApiService(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (apiRepository.findById(id).isPresent()) {
                Optional<Apis> api = apiRepository.findById(id);
                map.put("data", api);
                map.put("status", ErrorEnum.API_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_QUERY_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> modifyApiService(HttpSession httpSession, int id, Apis api){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                apis.setUrl(api.getUrl());
                apis.setUpdate_time(new Date(System.currentTimeMillis()));
                apis.setMethod(api.getMethod());
                apis.setNote(api.getNote());
                apis.setIs_cookie(api.getIs_cookie());
                apiRepository.saveAndFlush(apis);
                map.put("status", ErrorEnum.API_MODIFY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.API_MODIFY_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> deleteApiService(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
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
        }
        return () -> map;
    }

    @Override
    public Callable<Object> execApiService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("SESSION");
        List<Cases> casesList = caseRepository.findByApiId(id);
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                //根据case的数量起对应数量的多线程
                casesList.forEach(cases -> new Thread(() -> apicase(apis, cases)).start());
            }
            map.put("status", ErrorEnum.HTTP_EXEC_SUCCESS.getStatus());
            map.put("message", ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
        }
        return () -> map;
    }

    @Override
    public Callable<Object> execApiServiceOne(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("SESSION");
        List<Cases> casesList = caseRepository.findByApiId(id);
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (apiRepository.findById(id).isPresent()) {
                Apis apis = apiRepository.findById(id).get();
                for (Cases aCasesList : casesList) {
                    apicase(apis, aCasesList);
                }
            }
            map.put("status", ErrorEnum.HTTP_EXEC_SUCCESS.getStatus());
            map.put("message", ErrorEnum.HTTP_EXEC_SUCCESS.getMessage());
        }
        return () -> map;
    }

    private void apicase(Apis apis, Cases aCasesList) {
        //向外部发送http请求
        lock.lock();
        ClientResponse response = restHttp(apis, aCasesList).exchange().block();
        //把请求的结果保存到响应日志里
        Logs logs = new Logs();
        logs.setRequest_data(aCasesList.getRequest_data());
        logs.setRequest_time(new Timestamp(System.currentTimeMillis()));
        logs.setCode(Objects.requireNonNull(response).rawStatusCode());
        logs.setResponse_header(String.valueOf(Objects.requireNonNull(response).headers().asHttpHeaders()));
        logs.setResponse_data(response.bodyToMono(String.class).block());
        logs.setApiId(apis.getId());
        logs.setNote(aCasesList.getNote());
        logRepository.save(logs);
        lock.unlock();
    }

    private WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        String method = api.getMethod();
        String url =  api.getUrl();
        String data = cases.getRequest_data();
        boolean isCookie = api.getIs_cookie();
        WebClient.RequestHeadersSpec<?> body = null;
        switch (method){
            case "get":
                body = RestRequest.doGet(url, data, isCookie);
                break;
            case "post":
                body = RestRequest.doPost(url, data, isCookie);
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

    private void setApiTimeDefault(Apis api) {
        if (api.getCreate_time() == null) {
            api.setCreate_time(new Date(System.currentTimeMillis()));
        }
        if (api.getUpdate_time() == null) {
            api.setUpdate_time(new Date(System.currentTimeMillis()));
        }
    }
}
