package com.apitest.inf;

import com.apitest.entity.Cases;
import com.apitest.util.ServerResponse;


public interface CaseServiceInf {
    ServerResponse queryCaseByApiIdService(int apiId, int page, int size);

    ServerResponse queryOneCaseService(int id);

    ServerResponse deleteAllCaseByApiIdService(int apiId);

    ServerResponse modifyCaseService(int id, Cases cases);

    ServerResponse deleteOneCaseService(int id);

    ServerResponse addCaseByApiIdService(Cases cases, int apiId);
}
