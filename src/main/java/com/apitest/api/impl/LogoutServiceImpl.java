package com.apitest.api.impl;

import com.apitest.api.LogoutService;
import com.apitest.error.ErrorEnum;
import com.apitest.util.ServerResponse;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Override
    public ServerResponse logoutService(HttpServletRequest request) throws ServletException {
        request.logout();
        request.getSession().invalidate();
        return new ServerResponse(ErrorEnum.LOGOUT_SUCCESS.getStatus(), ErrorEnum.LOGOUT_SUCCESS.getMessage());
    }
}
