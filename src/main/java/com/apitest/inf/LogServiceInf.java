package com.apitest.inf;


public interface LogServiceInf {
    Object queryPageLogByApiIdService(int apiId, int page, int size);

    Object deleteOneLogService(int id);

    Object deleteAllLogByApiIdService(int apiId);
}
