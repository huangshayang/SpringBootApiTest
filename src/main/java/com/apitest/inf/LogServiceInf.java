package com.apitest.inf;


import com.apitest.util.ServerResponse;

/**
 * @author huangshayang
 */
public interface LogServiceInf {
    ServerResponse queryAllLogService(int page, int size);

    ServerResponse deleteOneLogService(int id);

    ServerResponse deleteAllLogService();

    ServerResponse deleteAllLogByApiIdService(int apiId);

    ServerResponse queryAllLogByApiIdService(int apiId, int page, int size);
}
