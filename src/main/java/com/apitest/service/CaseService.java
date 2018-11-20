package com.apitest.service;


import com.apitest.entity.Cases;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.CaseServiceInf;
import com.apitest.repository.ApiRepository;
import com.apitest.repository.CaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class CaseService implements CaseServiceInf {
    private final CaseRepository caseRepository;
    private final ApiRepository apiRepository;

    @Autowired
    public CaseService(CaseRepository caseRepository, ApiRepository apiRepository) {
        this.caseRepository = caseRepository;
        this.apiRepository = apiRepository;
    }

    @Override
    public Object queryCaseByApiIdService(HttpSession httpSession, int apiId){
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
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object queryOneCaseService(HttpSession httpSession, int id) {
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
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object deleteAllCaseByApiIdService(HttpSession httpSession, int apiId){
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
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object modifyCaseService(HttpSession httpSession, int id, Cases cases) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (caseRepository.findById(id).isPresent()){
                Cases cs = caseRepository.findById(id).get();
                cs.setRequestData(cases.getRequestData());
                cs.setUpdateTime(new Timestamp(System.currentTimeMillis()));
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
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object deleteOneCaseService(HttpSession httpSession, int id) {
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
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId) {
        Object sessionid = httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            //判断api是否存在
            if (apiRepository.findById(apiId).isPresent()) {
                cases.setApiId(apiId);
                caseRepository.save(cases);
                map.put("status", ErrorEnum.ADD_CASE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.ADD_CASE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.API_IS_NULL.getStatus());
                map.put("message", ErrorEnum.API_IS_NULL.getMessage());
            }
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }
}
