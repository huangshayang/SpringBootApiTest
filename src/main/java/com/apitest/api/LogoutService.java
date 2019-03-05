package com.apitest.api;

import com.apitest.util.ServerResponse;

import javax.servlet.http.HttpSession;

public interface LogoutService {
    ServerResponse logoutService(HttpSession httpSession);
}
