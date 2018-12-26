package com.apitest.service;


import com.apitest.entity.Apis;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ApiServiceInf;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import com.apitest.repository.EnvRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class ApiService implements ApiServiceInf {
    private final ApiRepository apiRepository;
    private final CaseRepository caseRepository;
    private final EnvRepository envRepository;
    private ReentrantLock lock = new ReentrantLock();
    private static ServerResponse serverResponse;

    @Autowired
    public ApiService(ApiRepository apiRepository, CaseRepository caseRepository, EnvRepository envRepository) {
        this.apiRepository = apiRepository;
        this.caseRepository = caseRepository;
        this.envRepository = envRepository;
    }

    @Override
    public ServerResponse addApiService(Apis api){
        log.info("参数: " + api);
        try {
            if (isBlank(api.getMethod())) {
                serverResponse = new ServerResponse(ErrorEnum.API_METHOD_IS_EMPTY.getStatus(), ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
            }else if (isBlank(api.getUrl())) {
                serverResponse = new ServerResponse(ErrorEnum.API_URL_IS_EMPTY.getStatus(), ErrorEnum.API_URL_IS_EMPTY.getMessage());
            }else if (isBlank(api.getCookie().toString())) {
                serverResponse = new ServerResponse(ErrorEnum.API_COOKIE_IS_EMPTY.getStatus(), ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
            }else if (isBlank(api.getName())) {
                serverResponse = new ServerResponse(ErrorEnum.API_NOTE_IS_EMPTY.getStatus(), ErrorEnum.API_NOTE_IS_EMPTY.getMessage());
            }else if (envRepository.findById(api.getEnvId()).isEmpty()) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_IS_NULL.getStatus(), ErrorEnum.ENV_IS_NULL.getMessage());
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
            //根据传递的id查询是否存在api
            Optional<Apis> apisOptional = apiRepository.findById(id);
            if (apisOptional.isPresent()) {
                //根据传递的参数里的url和method，查找是否存在对应的api
                Apis apisByUrlAndMethod = apiRepository.findByUrlAndMethod(api.getUrl(), api.getMethod());
                if (isBlank(api.getUrl())) {
                    serverResponse = new ServerResponse(ErrorEnum.API_URL_IS_EMPTY.getStatus(), ErrorEnum.API_URL_IS_EMPTY.getMessage());
                }else if (isBlank(api.getMethod())) {
                    serverResponse = new ServerResponse(ErrorEnum.API_METHOD_IS_EMPTY.getStatus(), ErrorEnum.API_METHOD_IS_EMPTY.getMessage());
                }else if (isBlank(api.getCookie().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.API_COOKIE_IS_EMPTY.getStatus(), ErrorEnum.API_COOKIE_IS_EMPTY.getMessage());
                }else if (isBlank(api.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.API_NOTE_IS_EMPTY.getStatus(), ErrorEnum.API_NOTE_IS_EMPTY.getMessage());
                }else if (envRepository.findById(api.getEnvId()).isEmpty()) {
                    serverResponse = new ServerResponse(ErrorEnum.ENV_IS_NULL.getStatus(), ErrorEnum.ENV_IS_NULL.getMessage());
                }else if (apisByUrlAndMethod != null && apisByUrlAndMethod.getId() != id) {
                    //如果能根据传递的参数里的url和method找到对应的api，且该api的id不等于传递的id，那么判断为该api重复，无法修改
                    serverResponse = new ServerResponse(ErrorEnum.API_IS_REPEAT.getStatus(), ErrorEnum.API_IS_REPEAT.getMessage());
                }else {
                    Apis apis = apisOptional.get();
                    apis.setUrl(api.getUrl());
                    apis.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    apis.setMethod(api.getMethod());
                    apis.setName(api.getName());
                    apis.setCookie(api.getCookie());
                    apis.setEnvId(api.getEnvId());
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
            lock.lock();
            if (apiRepository.findById(id).isPresent()) {
                caseRepository.deleteByApiId(id);
                apiRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.API_DELETE_SUCCESS.getStatus(), ErrorEnum.API_DELETE_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            }
            lock.unlock();
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
