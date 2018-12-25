package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import com.apitest.util.revertUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Log4j2
public class UserService {

    public ServerResponse userInfoService(HttpServletRequest request){
        ServerResponse serverResponse;
        try {
            String cookie = revertUtil.cookieToMap(request.getHeader("cookie"));
            User user = (User) request.getSession().getAttribute(cookie);
            serverResponse = new ServerResponse<>(1, "获取用户信息成功", user);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return serverResponse;
    }
}
