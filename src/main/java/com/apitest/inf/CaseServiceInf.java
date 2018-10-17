package com.apitest.inf;

import com.apitest.entity.Cases;

import javax.servlet.http.HttpSession;

public interface CaseServiceInf {
    Object queryCaseByApiIdService(HttpSession httpSession, int apiId);

    Object queryOneCaseService(HttpSession httpSession, int id);

    Object deleteAllCaseByApiIdService(HttpSession httpSession, int apiId);

    Object modifyCaseService(HttpSession httpSession, int id, Cases cases);

    Object deleteOneCaseService(HttpSession httpSession, int id);

    Object addCaseByApiIdService(HttpSession httpSession, Cases cases, int apiId);
}
