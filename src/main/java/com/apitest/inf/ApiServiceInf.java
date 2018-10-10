package com.apitest.inf;

import com.apitest.entity.Apis;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

public interface ApiServiceInf {

    Callable<Object> addApiService(HttpSession httpSession, Apis api);

    Callable<Object> queryPageApiService(HttpSession httpSession, int page, int size);

    Callable<Object> queryOneApiService(HttpSession httpSession, int id);

    Callable<Object> modifyApiService(HttpSession httpSession, int id, Apis api);

    Callable<Object> deleteApiService(HttpSession httpSession, int id);

    Callable<Object> execApiService(HttpSession httpSession, int id);

    Callable<Object> execApiServiceOne(HttpSession httpSession, int id);
}
