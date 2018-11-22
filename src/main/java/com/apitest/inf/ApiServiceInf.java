package com.apitest.inf;

import com.apitest.entity.Apis;
import com.apitest.util.ServerResponse;


public interface ApiServiceInf {

    ServerResponse addApiService(Apis api);

    ServerResponse queryPageApiService(int page, int size);

    ServerResponse queryOneApiService(int id);

    ServerResponse modifyApiService(int id, Apis api);

    ServerResponse deleteApiService(int id);

    ServerResponse execApiService(int id);

    ServerResponse execApiServiceOne(int id);
}
