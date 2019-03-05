package com.apitest.api.impl;

import com.apitest.api.LogoutService;
import com.apitest.error.ErrorEnum;
import com.apitest.util.ServerResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Override
    public ServerResponse logoutService(HttpSession httpSession) {
        httpSession.invalidate();
        return new ServerResponse(ErrorEnum.LOGOUT_SUCCESS.getStatus(), ErrorEnum.LOGOUT_SUCCESS.getMessage());
    }
}
