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
import com.apitest.util.JwtUtil;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Async
@Log4j2
public class ApiService implements ApiServiceInf {
    private final ApiRepository apiRepository;
    private final CaseRepository caseRepository;
    private final LogRepository logRepository;
    private ReentrantLock lock = new ReentrantLock();
    private final String PAYLOAD_KEY = "apitest";

    @Autowired
    public ApiService(ApiRepository apiRepository, CaseRepository caseRepository, LogRepository logRepository) {
        this.apiRepository = apiRepository;
        this.caseRepository = caseRepository;
        this.logRepository = logRepository;
    }

    @Override
    public CompletableFuture<Object> addApiService(HttpServletRequest request, Apis api){
        Map<String, Object> map = new HashMap<>(8);
        try {
            log.info("参数: " + api);
            String jwt = request.getHeader("auth");
            if (jwt == null || !Objects.equals(PAYLOAD_KEY, JwtUtil.parseJWT(jwt).get("info", String.class))) {
                map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
            }else {
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
                    setApiTimeDefault(api);
                    apiRepository.save(api);
                    map.put("status", ErrorEnum.API_ADD_SUCCESS.getStatus());
                    map.put("message", ErrorEnum.API_ADD_SUCCESS.getMessage());
                }
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (ExpiredJwtException | SignatureException | MalformedJwtException e){
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }catch (Exception e){
            new ExceptionLog(e, api);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> queryPageApiService(HttpServletRequest request, int page, int size){
        Map<String, Object> map = new HashMap<>(8);
        try {
            String jwt = request.getHeader("auth");
            if (jwt.isEmpty() || jwt.isBlank() || !Objects.equals(PAYLOAD_KEY, JwtUtil.parseJWT(jwt).get("info", String.class))) {
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
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (ExpiredJwtException | SignatureException | MalformedJwtException e){
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, page, size);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> queryOneApiService(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("user");
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
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> modifyApiService(HttpSession httpSession, int id, Apis api){
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
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
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> deleteApiService(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("user");
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
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> execApiService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("user");
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
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> execApiServiceOne(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("user");
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

    private void setApiTimeDefault(Apis api) {
        if (api.getCreateTime() == null) {
            api.setCreateTime(new Date(System.currentTimeMillis()));
        }
        if (api.getUpdateTime() == null) {
            api.setUpdateTime(new Date(System.currentTimeMillis()));
        }
    }
}
