package com.apitest.inf;

import com.apitest.entity.Apis;


public interface ApiServiceInf {

    Object addApiService(Apis api);

    Object queryPageApiService(int page, int size);

    Object queryOneApiService(int id);

    Object modifyApiService(int id, Apis api);

    Object deleteApiService(int id);

    Object execApiService(int id);

    Object execApiServiceOne(int id);
}
