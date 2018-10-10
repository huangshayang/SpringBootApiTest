package com.apitest.service;


import com.apitest.entity.Cases;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.CaseServiceInf;
import com.apitest.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

@Service
public class CaseService implements CaseServiceInf {
    private final CaseRepository caseRepository;

    @Autowired
    private CaseService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public Callable<Object> queryCaseByApiIdService(HttpSession httpSession, int apiId){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            List<Cases> cases = caseRepository.findByApiId(apiId);
            map.put("status", ErrorEnum.CASE_QUERY_SUCCESS.getStatus());
            map.put("message", ErrorEnum.CASE_QUERY_SUCCESS.getMessage());
            map.put("data", cases);
        }
        return () -> map;
    }

    @Override
    public Callable<Object> queryOneCaseService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("SESSION");
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
        return () -> map;
    }

    @Override
    public Callable<Object> deleteAllCaseByApiIdService(HttpSession httpSession, int apiId){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            caseRepository.deleteByApiId(apiId);
            map.put("status", ErrorEnum.CASE_DELETE_SUCCESS.getStatus());
            map.put("message", ErrorEnum.CASE_DELETE_SUCCESS.getMessage());
        }
        return () -> map;
    }

    @Override
    public Callable<Object> modifyCaseService(HttpSession httpSession, int id, Cases cases) {
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (caseRepository.findById(id).isPresent()){
                Cases cs = caseRepository.findById(id).get();
                cs.setRequest_data(cases.getRequest_data());
                cs.setUpdate_time(new Date(System.currentTimeMillis()));
                cs.setNote(cases.getNote());
                cs.setExpect_result(cases.getExpect_result());
                caseRepository.saveAndFlush(cs);
                map.put("status", ErrorEnum.MODIFY_CASE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.MODIFY_CASE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.CASE_IS_NULL.getStatus());
                map.put("message", ErrorEnum.CASE_IS_NULL.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> deleteOneCaseService(HttpSession httpSession, int id) {
        Object sessionid = httpSession.getAttribute("SESSION");
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
        return () -> map;
    }

    @Override
    public Callable<Object> addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId) {
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            cases.setApiId(apiId);
            setApiTimeDefault(cases);
            caseRepository.save(cases);
            map.put("status", ErrorEnum.ADD_CASE_SUCCESS.getStatus());
            map.put("message", ErrorEnum.ADD_CASE_SUCCESS.getMessage());
        }
        return () -> map;
    }

    private void setApiTimeDefault(Cases cases) {
        if (cases.getCreate_time() == null) {
            cases.setCreate_time(new Date(System.currentTimeMillis()));
        }
        if (cases.getUpdate_time() == null) {
            cases.setUpdate_time(new Date(System.currentTimeMillis()));
        }

    }
}
