package com.apitest.inf;


import com.apitest.util.ServerResponse;

/**
 * @author huangshayang
 */
public interface LogServiceInf {
    ServerResponse queryPageLogByApiIdService(int apiId, int page, int size);

    ServerResponse deleteOneLogService(int id);

    ServerResponse deleteAllLogByApiIdService(int apiId);
}
