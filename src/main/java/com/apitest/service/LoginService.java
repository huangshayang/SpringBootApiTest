package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author huangshayang
 */
@Service
public class LoginService {
    private final UserRepository userRepository;

    @Autowired
    private LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Callable<Object> loginService(Map<String, Object> models, HttpSession httpSession){
        Map<String, Object> map = new HashMap<>(8);
        String code = String.valueOf(httpSession.getAttribute("number"));
        Object username = models.get("username");
        Object password = models.get("password");
        Object captcha = models.get("captcha");
        if (captcha == null || username == null || password == null ||
                !(username.getClass().equals(String.class)) ||
                !(password.getClass().equals(String.class)) ||
                !(captcha.getClass().equals(String.class))
        ) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            httpSession.invalidate();
        }else if (!userRepository.existsByUsername(username)) {
            map.put("status", ErrorEnum.USER_IS_NOT_EXISTS.getStatus());
            map.put("message", ErrorEnum.USER_IS_NOT_EXISTS.getMessage());
            httpSession.invalidate();
        } else if (!code.equalsIgnoreCase(String.valueOf(captcha))) {
            map.put("status", ErrorEnum.CAPTCHA_ERROR.getStatus());
            map.put("message", ErrorEnum.CAPTCHA_ERROR.getMessage());
            httpSession.invalidate();
        }else if (Objects.equals("", username) || Objects.equals("", password)){
            map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            httpSession.invalidate();
        }else if (!new BCryptPasswordEncoder().matches((CharSequence) password, userRepository.findByUsername(username).getPassword())) {
            map.put("status", ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus());
            map.put("message", ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
            httpSession.invalidate();
        }else {
            httpSession.removeAttribute("number");
            httpSession.setAttribute("SESSION", userRepository.findByUsername(username));
            httpSession.setMaxInactiveInterval(600);
            map.put("status", ErrorEnum.LOGIN_SUCCESS.getStatus());
            map.put("message", ErrorEnum.LOGIN_SUCCESS.getMessage());
        }
        return () -> map;
    }

    public Callable<Object> loginService1(User user, HttpSession httpSession){
        Map<String, Object> map = new HashMap<>(8);
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || password == null) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
        }else if (Objects.equals("", username) || Objects.equals("", password)){
            map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
        }else if (!new BCryptPasswordEncoder().matches(password, userRepository.findByUsername(username).getPassword())) {
            map.put("status", ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus());
            map.put("message", ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
        }else {
            httpSession.setAttribute("SESSION", userRepository.findByUsername(username));
            httpSession.setMaxInactiveInterval(300);
            map.put("status", ErrorEnum.LOGIN_SUCCESS.getStatus());
            map.put("message", ErrorEnum.LOGIN_SUCCESS.getMessage());
        }
        return () -> map;
    }

}
