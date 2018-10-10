package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ForgetServiceInf;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

@Service
public class ForgetService implements ForgetServiceInf {

    private final UserRepository userRepository;

    @Autowired
    private ForgetService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Callable<Object> forgetPasswordService(HttpSession httpSession, Map<String, Object> models) {
        Map<String, Object> map = new HashMap<>(8);
        String username = String.valueOf(models.get("username"));
        String password = String.valueOf(models.get("password"));
        String token = String.valueOf(models.get("token"));
        User user = userRepository.findByUsername(username);
        try {
            if (username == null || password == null || token == null) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (Objects.equals("", username) || Objects.equals("", password)) {
                map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (!userRepository.existsByUsername(username)) {
                map.put("status", ErrorEnum.USER_IS_NOT_EXIST.getStatus());
                map.put("message", ErrorEnum.USER_IS_NOT_EXIST.getMessage());
            }else if (Objects.equals("", token)) {
                map.put("status", ErrorEnum.TOKEN_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.TOKEN_IS_EMPTY.getMessage());
            }else if (!Objects.equals(token, httpSession.getAttribute(username).toString())) {
                map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
                map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
            }else {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.saveAndFlush(user);
                map.put("status", ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus());
                map.put("message", ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
                httpSession.invalidate();
            }
        }catch (Exception e) {
            map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
            httpSession.invalidate();
        }
        return () -> map;
    }

    @Override
    public Callable<Object> getTokenService(HttpSession httpSession, Map<String, Object> username) {
        Map<String, Object> map = new HashMap<>(8);
        String name = String.valueOf(username.get("username"));
        if (name == null) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
        }else if (Objects.equals("", name)) {
            map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
        }else if (!userRepository.existsByUsername(name)) {
            map.put("status", ErrorEnum.USER_IS_NOT_EXIST.getStatus());
            map.put("message", ErrorEnum.USER_IS_NOT_EXIST.getMessage());
        }else {
            httpSession.setAttribute(name, UUID.randomUUID().toString());
            map.put("status", ErrorEnum.TOKEN_SUSSCESS.getStatus());
            map.put("message", ErrorEnum.TOKEN_SUSSCESS.getMessage());
            map.put("data", httpSession.getAttribute(name).toString());
        }
        return () -> map;
    }
}
