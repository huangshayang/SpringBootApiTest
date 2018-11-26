package com.apitest.service;


import com.apitest.entity.Cases;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.CaseServiceInf;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class CaseService implements CaseServiceInf {
    private final CaseRepository caseRepository;
    private final ApiRepository apiRepository;
    private static ServerResponse serverResponse;

    @Autowired
    public CaseService(CaseRepository caseRepository, ApiRepository apiRepository) {
        this.caseRepository = caseRepository;
        this.apiRepository = apiRepository;
    }

    @Override
    public ServerResponse queryCaseByApiIdService(int apiId){
        try {
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                List<Cases> cases = caseRepository.findByApiId(apiId);
                serverResponse = new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), cases);
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
    public ServerResponse queryOneCaseService(int id) {
        try {
            Optional<Cases> cases = caseRepository.findById(id);
            serverResponse = new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), cases);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteAllCaseByApiIdService(int apiId){
        try {
            if (apiRepository.findById(apiId).isPresent()) {
                caseRepository.deleteByApiId(apiId);
                serverResponse = new ServerResponse(ErrorEnum.CASE_DELETE_SUCCESS.getStatus(), ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
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
    public ServerResponse modifyCaseService(int id, Cases cases) {
        try {
            log.info("参数: " + cases);
            if (caseRepository.findById(id).isPresent()){
                if (isBlank(cases.getRequestData())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_REQUESTDATA_IS_EMPTY.getStatus(), ErrorEnum.CASE_REQUESTDATA_IS_EMPTY.getMessage());
                }else if (isBlank(cases.getNote())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
                }else {
                    Cases cs = caseRepository.findById(id).get();
                    cs.setRequestData(cases.getRequestData());
                    cs.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    cs.setNote(cases.getNote());
                    cs.setExpectResult(cases.getExpectResult());
                    caseRepository.saveAndFlush(cs);
                    serverResponse = new ServerResponse(ErrorEnum.MODIFY_CASE_SUCCESS.getStatus(), ErrorEnum.MODIFY_CASE_SUCCESS.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.CASE_IS_NULL.getStatus(), ErrorEnum.CASE_IS_NULL.getMessage());
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
    public ServerResponse deleteOneCaseService(int id) {
        try {
            if (caseRepository.findById(id).isPresent()){
                caseRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.CASE_DELETE_SUCCESS.getStatus(), ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.CASE_IS_NULL.getStatus(), ErrorEnum.CASE_IS_NULL.getMessage());
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
    public ServerResponse addCaseByApiIdService(Cases cases, int apiId) {
        try {
            log.info("参数: " + cases);
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                if (isBlank(cases.getRequestData())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_REQUESTDATA_IS_EMPTY.getStatus(), ErrorEnum.CASE_REQUESTDATA_IS_EMPTY.getMessage());
                }else if (isBlank(cases.getNote())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
                }else {
                    cases.setApiId(apiId);
                    caseRepository.save(cases);
                    serverResponse = new ServerResponse(ErrorEnum.ADD_CASE_SUCCESS.getStatus(), ErrorEnum.ADD_CASE_SUCCESS.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.ADD_CASE_SUCCESS.getStatus(), ErrorEnum.ADD_CASE_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
