package com.apitest.service;


import com.apitest.error.ErrorEnum;
import com.apitest.util.ServerResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author huangshayang
 */
@Service
public class LogoutService {

    public ServerResponse logoutService(HttpSession httpSession){
        httpSession.invalidate();
        return new ServerResponse(ErrorEnum.LOGOUT_SUCCESS.getStatus(), ErrorEnum.LOGOUT_SUCCESS.getMessage());
    }
}
