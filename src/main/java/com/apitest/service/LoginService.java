package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.LoginServiceInf;
import com.apitest.repository.UserRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.apitest.configconsts.ConfigConsts.USERSESSION_KEY;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class LoginService implements LoginServiceInf {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ServerResponse loginService(HttpServletResponse response, HttpSession httpSession, String username, String password){
        ServerResponse serverResponse;
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            User u = userRepository.findUserByUsernameOrEmail(username, username);
            if (isBlank(username) || isBlank(password)) {
                serverResponse = new ServerResponse(ErrorEnum.PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
            }else if (u == null) {
                serverResponse = new ServerResponse(ErrorEnum.USER_IS_NOT_EXISTS.getStatus(), ErrorEnum.USER_IS_NOT_EXISTS.getMessage());
            }else if (!new BCryptPasswordEncoder().matches(password, u.getPassword())) {
                serverResponse = new ServerResponse(ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus(), ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
            }else {
                String session = new BCryptPasswordEncoder().encode(String.valueOf(u));
                httpSession.setAttribute(USERSESSION_KEY, session);
                Cookie resCookie = new Cookie(USERSESSION_KEY, session);
                resCookie.setHttpOnly(true);
                resCookie.setMaxAge(httpSession.getMaxInactiveInterval());
                resCookie.setPath("/");
                response.addCookie(resCookie);
                serverResponse = new ServerResponse(ErrorEnum.LOGIN_SUCCESS.getStatus(), ErrorEnum.LOGIN_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
