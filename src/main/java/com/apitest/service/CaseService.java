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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public ServerResponse queryCaseByApiIdService(int apiId, int page, int size) {
        try {
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                if (page < 0 || size <= 0) {
                    serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
                } else {
                    Sort sort = new Sort(Sort.Direction.ASC, "id");
                    Page<Cases> cases = caseRepository.findCasesByApiId(apiId, PageRequest.of(page, size, sort));
                    serverResponse = new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), cases);
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryAllCaseService(int page, int size) {
        try {
            if (page < 0 || size <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            } else {
                Sort sort = new Sort(Sort.Direction.ASC, "id");
                Page<Cases> cases = caseRepository.findAll(PageRequest.of(page, size, sort));
                serverResponse = new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), cases);
            }
        } catch (Exception e) {
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
            serverResponse = cases.map(cases1 -> new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), cases1)).orElseGet(() -> new ServerResponse<>(ErrorEnum.CASE_IS_NULL.getStatus(), ErrorEnum.CASE_IS_NULL.getMessage()));
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteAllCaseByApiIdService(int apiId) {
        try {
            if (apiRepository.findById(apiId).isPresent()) {
                caseRepository.deleteByApiId(apiId);
                serverResponse = new ServerResponse(ErrorEnum.CASE_DELETE_SUCCESS.getStatus(), ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            }
        } catch (Exception e) {
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
            Optional<Cases> optionalCases = caseRepository.findById(id);
            if (optionalCases.isPresent()) {
                if (isBlank(cases.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
                } else if (isBlank(cases.getAvailable().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getStatus(), ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getMessage());
                } else {
                    Cases cs = optionalCases.get();
                    cs.setJsonData(cases.getJsonData());
                    cs.setParamsData(cases.getParamsData());
                    cs.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    cs.setName(cases.getName());
                    cs.setExpectResult(cases.getExpectResult());
                    cs.setAvailable(cases.getAvailable());
                    caseRepository.saveAndFlush(cs);
                    serverResponse = new ServerResponse(ErrorEnum.CASE_MODIFY_SUCCESS.getStatus(), ErrorEnum.CASE_MODIFY_SUCCESS.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.CASE_IS_NULL.getStatus(), ErrorEnum.CASE_IS_NULL.getMessage());
            }
        } catch (Exception e) {
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
            if (caseRepository.findById(id).isPresent()) {
                caseRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.CASE_DELETE_SUCCESS.getStatus(), ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.CASE_IS_NULL.getStatus(), ErrorEnum.CASE_IS_NULL.getMessage());
            }
        } catch (Exception e) {
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
                if (isBlank(cases.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
                } else if (isBlank(cases.getAvailable().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getStatus(), ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getMessage());
                } else {
                    cases.setApiId(apiId);
                    caseRepository.save(cases);
                    serverResponse = new ServerResponse(ErrorEnum.ADD_CASE_SUCCESS.getStatus(), ErrorEnum.ADD_CASE_SUCCESS.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.ADD_CASE_SUCCESS.getStatus(), ErrorEnum.ADD_CASE_SUCCESS.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
