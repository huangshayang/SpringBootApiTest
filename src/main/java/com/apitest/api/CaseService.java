package com.apitest.api;

import com.apitest.entity.Cases;
import com.apitest.util.ServerResponse;

public interface CaseService {
    ServerResponse queryAllCaseService(int page, int size);

    ServerResponse queryOneCaseService(int id);

    ServerResponse modifyCaseService(int id, Cases cases);

    ServerResponse deleteCaseService(int id);

    ServerResponse addCaseService(Cases cases);
}
