package com.apitest.inf;

import com.apitest.entity.Cases;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

public interface CaseServiceInf {
    Callable<Object> queryCaseByApiIdService(HttpSession httpSession, int apiId);

    Callable<Object> queryOneCaseService(HttpSession httpSession, int id);

    Callable<Object> deleteAllCaseByApiIdService(HttpSession httpSession, int apiId);

    Callable<Object> modifyCaseService(HttpSession httpSession, int id, Cases cases);

    Callable<Object> deleteOneCaseService(HttpSession httpSession, int id);

    Callable<Object> addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId);
}
