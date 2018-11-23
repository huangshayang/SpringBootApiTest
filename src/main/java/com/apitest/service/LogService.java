package com.apitest.service;

import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.LogServiceInf;
import com.apitest.repository.LogRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class LogService implements LogServiceInf {

    private final LogRepository logRepository;
    private static ServerResponse serverResponse;

    @Autowired
    public LogService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @Override
    public ServerResponse queryPageLogByApiIdService(int apiId, int page, int size){
        try {
            if (page <0 || size <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            }else {
                Sort sort = new Sort(Sort.Direction.ASC, "id");
                Page<Logs> logs = logRepository.findAllByApiId(apiId, PageRequest.of(page, size, sort));
                serverResponse = new ServerResponse<>(ErrorEnum.LOG_QUERY_SUCCESS.getStatus(), ErrorEnum.LOG_QUERY_SUCCESS.getMessage(), logs);
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
    public ServerResponse deleteOneLogService(int id){
        try {
            if (logRepository.findById(id).isPresent()) {
                logRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.LOG_DELETE_SUCCESS.getStatus(), ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.LOG_IS_NULL.getStatus(), ErrorEnum.LOG_IS_NULL.getMessage());
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
    public ServerResponse deleteAllLogByApiIdService(int apiId) {
        try {
            logRepository.deleteByApiId(apiId);
            serverResponse = new ServerResponse(ErrorEnum.LOG_DELETE_SUCCESS.getStatus(), ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
