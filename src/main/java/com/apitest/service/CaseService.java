package com.apitest.service;


import com.apitest.entity.Cases;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.CaseServiceInf;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Async
public class CaseService implements CaseServiceInf {
    private final CaseRepository caseRepository;
    private final ApiRepository apiRepository;

    @Autowired
    public CaseService(CaseRepository caseRepository, ApiRepository apiRepository) {
        this.caseRepository = caseRepository;
        this.apiRepository = apiRepository;
    }

    @Override
    public CompletableFuture<Object> queryCaseByApiIdService(HttpSession httpSession, int apiId){
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                List<Cases> cases = caseRepository.findByApiId(apiId);
                map.put("status", ErrorEnum.CASE_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.CASE_QUERY_SUCCESS.getMessage());
                map.put("data", cases);
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> queryOneCaseService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            Optional<Cases> cases = caseRepository.findById(id);
            map.put("status", ErrorEnum.CASE_QUERY_SUCCESS.getStatus());
            map.put("message", ErrorEnum.CASE_QUERY_SUCCESS.getMessage());
            map.put("data", cases);
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> deleteAllCaseByApiIdService(HttpSession httpSession, int apiId){
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            caseRepository.deleteByApiId(apiId);
            map.put("status", ErrorEnum.CASE_DELETE_SUCCESS.getStatus());
            map.put("message", ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> modifyCaseService(HttpSession httpSession, int id, Cases cases) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (caseRepository.findById(id).isPresent()){
                Cases cs = caseRepository.findById(id).get();
                cs.setRequestData(cases.getRequestData());
                cs.setUpdateTime(new Date(System.currentTimeMillis()));
                cs.setNote(cases.getNote());
                cs.setExpectResult(cases.getExpectResult());
                caseRepository.saveAndFlush(cs);
                map.put("status", ErrorEnum.MODIFY_CASE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.MODIFY_CASE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.CASE_IS_NULL.getStatus());
                map.put("message", ErrorEnum.CASE_IS_NULL.getMessage());
            }
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> deleteOneCaseService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (caseRepository.findById(id).isPresent()){
                caseRepository.deleteById(id);
                map.put("status", ErrorEnum.CASE_DELETE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.CASE_IS_NULL.getStatus());
                map.put("message", ErrorEnum.CASE_IS_NULL.getMessage());
            }
        }
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<Object> addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                cases.setApiId(apiId);
                setApiTimeDefault(cases);
                caseRepository.save(cases);
                map.put("status", ErrorEnum.ADD_CASE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.ADD_CASE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
        }
        return CompletableFuture.completedFuture(map);
    }

    private void setApiTimeDefault(Cases cases) {
        if (cases.getCreateTime() == null) {
            cases.setCreateTime(new Date(System.currentTimeMillis()));
        }
        if (cases.getUpdateTime() == null) {
            cases.setUpdateTime(new Date(System.currentTimeMillis()));
        }

    }
}
