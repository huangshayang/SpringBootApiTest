package com.apitest.api;

import com.apitest.util.ServerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface LogoutService {
    ServerResponse logoutService(HttpServletRequest request) throws ServletException;
}
