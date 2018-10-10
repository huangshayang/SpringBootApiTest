package com.apitest.inf;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

public interface LogServiceInf {
    Callable<Object> queryPageLogByApiIdService(HttpSession httpSession, int apiId, int page, int size);

    Callable<Object> deleteOneLogService(HttpSession httpSession, int id);

    Callable<Object> deleteAllLogByApiIdService(HttpSession httpSession, int apiId);
}
