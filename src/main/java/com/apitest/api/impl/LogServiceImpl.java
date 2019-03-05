package com.apitest.api.impl;

import com.apitest.api.LogService;
import com.apitest.entity.Logs;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.LogMapper;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Log4j2
public class LogServiceImpl implements LogService {

    private static ServerResponse serverResponse;

    @Resource
    private LogMapper logMapper;

    @Override
    public ServerResponse queryAllLogService(int page, int size) {
        try {
            if (page < 0 || size <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            } else {
                PageHelper.startPage(page, size);
                List<Logs> logsList = logMapper.findAllLog();
                PageInfo<Logs> logsPageInfo = new PageInfo<>(logsList);
                serverResponse = new ServerResponse<>(ErrorEnum.LOG_QUERY_SUCCESS.getStatus(), ErrorEnum.LOG_QUERY_SUCCESS.getMessage(), logsPageInfo);
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
    public ServerResponse deleteLogService(int id) {
        try {
            if (logMapper.findById(id).isPresent()) {
                logMapper.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.LOG_DELETE_SUCCESS.getStatus(), ErrorEnum.LOG_DELETE_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.LOG_IS_NULL.getStatus(), ErrorEnum.LOG_IS_NULL.getMessage());
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
