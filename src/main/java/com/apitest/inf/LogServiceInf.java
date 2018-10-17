package com.apitest.inf;

import javax.servlet.http.HttpSession;

public interface LogServiceInf {
    Object queryPageLogByApiIdService(HttpSession httpSession, int apiId, int page, int size);

    Object deleteOneLogService(HttpSession httpSession, int id);

    Object deleteAllLogByApiIdService(HttpSession httpSession, int apiId);
}
