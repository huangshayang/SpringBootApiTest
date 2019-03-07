package com.apitest.api.impl;


import com.apitest.api.CaseService;
import com.apitest.entity.Cases;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.ApiMapper;
import com.apitest.mapper.CaseMapper;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class CaseServiceImpl implements CaseService {

    private static ServerResponse serverResponse;

    @Resource
    private CaseMapper caseMapper;

    @Resource
    private ApiMapper apiMapper;

    @Override
    public ServerResponse queryAllCaseService(int page, int size) {
        try {
            if (page < 0 || size <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            } else {
                PageHelper.startPage(page, size);
                List<Cases> casesList = caseMapper.findAllCase();
                PageInfo<Cases> casesPageInfo = new PageInfo<>(casesList);
                serverResponse = new ServerResponse<>(ErrorEnum.CASE_QUERY_SUCCESS.getStatus(), ErrorEnum.CASE_QUERY_SUCCESS.getMessage(), casesPageInfo);
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
            Optional<Cases> cases = caseMapper.findById(id);
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
    public ServerResponse modifyCaseService(int id, Cases cases) {
        try {
            log.info("参数: " + cases + ", " + id);
            Optional<Cases> optionalCases = caseMapper.findById(id);
            if (optionalCases.isPresent()) {
                if (isBlank(cases.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
                } else if (isBlank(cases.getAvailable().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getStatus(), ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getMessage());
                } else if (apiMapper.findById(cases.getApiId()).isEmpty()) {
                    serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
                }else {
                    Cases cs = optionalCases.get();
                    cs.setJsonData(cases.getJsonData());
                    cs.setParamsData(cases.getParamsData());
                    cs.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    cs.setName(cases.getName());
                    cs.setAvailable(cases.getAvailable());
                    cs.setApiId(cases.getApiId());
                    caseMapper.update(cs, id);
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
    public ServerResponse deleteCaseService(int id) {
        try {
            if (caseMapper.findById(id).isPresent()) {
                caseMapper.deleteById(id);
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
    public ServerResponse addCaseService(Cases cases) {
        try {
            log.info("参数: " + cases);
            if (isBlank(cases.getName())) {
                serverResponse = new ServerResponse(ErrorEnum.CASE_NOTE_IS_EMPTY.getStatus(), ErrorEnum.CASE_NOTE_IS_EMPTY.getMessage());
            } else if (isBlank(cases.getAvailable().toString())) {
                serverResponse = new ServerResponse(ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getStatus(), ErrorEnum.CASE_AVAILABLE_IS_EMPTY.getMessage());
            } else if (apiMapper.findById(cases.getApiId()).isEmpty()) {
                serverResponse = new ServerResponse(ErrorEnum.API_IS_NULL.getStatus(), ErrorEnum.API_IS_NULL.getMessage());
            } else {
                caseMapper.save(cases);
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
