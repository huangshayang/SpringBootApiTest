package com.apitest.inf;

import com.apitest.entity.Apis;

import javax.servlet.http.HttpSession;

public interface ApiServiceInf {

    Object addApiService(HttpSession httpSession, Apis api);

    Object queryPageApiService(HttpSession httpSession, int page, int size);

    Object queryOneApiService(HttpSession httpSession, int id);

    Object modifyApiService(HttpSession httpSession, int id, Apis api);

    Object deleteApiService(HttpSession httpSession, int id);

    Object execApiService(HttpSession httpSession, int id);

    Object execApiServiceOne(HttpSession httpSession, int id);
}
