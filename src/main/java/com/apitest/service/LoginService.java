package com.apitest.service;

import com.apitest.error.ErrorEnum;
import com.apitest.inf.LoginServiceInf;
import com.apitest.repository.UserRepository;
import com.apitest.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Service
@Async
@Log4j2
public class LoginService implements LoginServiceInf {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<Object> loginService(String username, String password){
        Map<String, Object> map = new HashMap<>(8);
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            if (username == null || password == null || username.getClass() != String.class || password.getClass() != String.class) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (!userRepository.existsByUsername(username)) {
                map.put("status", ErrorEnum.USER_IS_NOT_EXISTS.getStatus());
                map.put("message", ErrorEnum.USER_IS_NOT_EXISTS.getMessage());
            }else if (Objects.equals("", password)) {
                map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (!new BCryptPasswordEncoder().matches(password, userRepository.findByUsername(username).getPassword())) {
                map.put("status", ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus());
                map.put("message", ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
            }else {
                String jwt = JwtUtil.createJWT(username);
                map.put("status", ErrorEnum.LOGIN_SUCCESS.getStatus());
                map.put("message", ErrorEnum.LOGIN_SUCCESS.getMessage());
                map.put("auth", jwt);
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
//            new ExceptionLog(e, models);
        }
        return CompletableFuture.completedFuture(map);
    }
}
