package com.apitest.service;

import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.LogServiceInf;
import com.apitest.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class LogService implements LogServiceInf {

    private final LogRepository logRepository;

    @Autowired
    private LogService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @Override
    public Callable<Object> queryPageLogByApiIdService(HttpSession httpSession, int apiId, int page, int size){
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
                Page<Logs> logs = logRepository.findAllByApiId(apiId, PageRequest.of(page, size, sort));
                map.put("data", logs);
                map.put("status", ErrorEnum.LOG_QUERY_SUCCESS.getStatus());
                map.put("message", ErrorEnum.LOG_QUERY_SUCCESS.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> deleteOneLogService(HttpSession httpSession, int id){
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            if (logRepository.findById(id).isPresent()) {
                logRepository.deleteById(id);
                map.put("status", ErrorEnum.LOG_DELETE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.LOG_IS_NULL.getStatus());
                map.put("message", ErrorEnum.LOG_IS_NULL.getMessage());
            }
        }
        return () -> map;
    }

    @Override
    public Callable<Object> deleteAllLogByApiIdService(HttpSession httpSession, int apiId) {
        Object sessionid = httpSession.getAttribute("SESSION");
        Map<String, Object> map = new HashMap<>(8);
        if (sessionid == null) {
            map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
            map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
        }else {
            logRepository.deleteByApiId(apiId);
            map.put("status", ErrorEnum.LOG_DELETE_SUCCESS.getStatus());
            map.put("message", ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
        }
        return () -> map;
    }
}
