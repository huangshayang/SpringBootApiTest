package com.apitest.service;

import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.LogServiceInf;
import com.apitest.repository.LogRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class LogService implements LogServiceInf {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @Override
    public Object queryPageLogByApiIdService(int apiId, int page, int size){
        Map<String, Object> map = new HashMap<>(8);
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object deleteOneLogService(int id){
        Map<String, Object> map = new HashMap<>(8);
        try {
            if (logRepository.findById(id).isPresent()) {
                logRepository.deleteById(id);
                map.put("status", ErrorEnum.LOG_DELETE_SUCCESS.getStatus());
                map.put("message", ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
            }else {
                map.put("status", ErrorEnum.LOG_IS_NULL.getStatus());
                map.put("message", ErrorEnum.LOG_IS_NULL.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }

    @Override
    public Object deleteAllLogByApiIdService(int apiId) {
        Map<String, Object> map = new HashMap<>(8);
        try {
            logRepository.deleteByApiId(apiId);
            map.put("status", ErrorEnum.LOG_DELETE_SUCCESS.getStatus());
            map.put("message", ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }
}
