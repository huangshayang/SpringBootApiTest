package com.apitest.service;

import com.apitest.error.ErrorEnum;
import com.apitest.inf.LoginServiceInf;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    public Object loginService(HttpServletResponse response, HttpSession httpSession, String username, String password){
        Map<String, Object> map = new HashMap<>(8);
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            if (username == null || password == null || password.getClass() != String.class) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (userRepository.findUserByUsernameOrEmail(username, username) == null) {
                map.put("status", ErrorEnum.USER_IS_NOT_EXISTS.getStatus());
                map.put("message", ErrorEnum.USER_IS_NOT_EXISTS.getMessage());
            }else if (password.isEmpty() || password.isBlank()) {
                map.put("status", ErrorEnum.PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
            }else if (!new BCryptPasswordEncoder().matches(password, userRepository.findUserByUsernameOrEmail(username, username).getPassword())) {
                map.put("status", ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus());
                map.put("message", ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
            }else {
                String session = new BCryptPasswordEncoder().encode(String.valueOf(userRepository.findUserByUsernameOrEmail(username, username)));
                httpSession.setAttribute("user_session", session);
                Cookie resCookie = new Cookie("user_session", session);
                resCookie.setPath("/");
                resCookie.setHttpOnly(true);
                resCookie.setMaxAge(httpSession.getMaxInactiveInterval());
                response.addCookie(resCookie);
                map.put("status", ErrorEnum.LOGIN_SUCCESS.getStatus());
                map.put("message", ErrorEnum.LOGIN_SUCCESS.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }
}
