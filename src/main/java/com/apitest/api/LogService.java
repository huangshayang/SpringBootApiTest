package com.apitest.api;

import com.apitest.util.ServerResponse;

public interface LogService {
    ServerResponse queryAllLogService(int page, int size);

    ServerResponse deleteLogService(int id);

    ServerResponse querySearchLogService(long startTime, long endTime);
}
