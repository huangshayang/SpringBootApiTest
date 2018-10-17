package com.apitest.service;

import com.apitest.error.ErrorEnum;
import com.apitest.inf.LoginServiceInf;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huangshayang
 */
@Service
public class LoginService implements LoginServiceInf {
    private final UserRepository userRepository;

    @Autowired
    private LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object loginService(HttpSession httpSession, Map<String, String> models){
        Map<String, Object> map = new HashMap<>(8);
        String code = String.valueOf(httpSession.getAttribute("captcha"));
        String username = models.get("username");
        String password = models.get("password");
        String captcha = models.get("captcha");
        if (captcha == null || username == null || password == null ||
                username.getClass() != String.class ||
                password.getClass() != String.class ||
                captcha.getClass() != String.class) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
        }else if (!userRepository.existsByUsername(username)) {
            map.put("status", ErrorEnum.USER_IS_NOT_EXISTS.getStatus());
            map.put("message", ErrorEnum.USER_IS_NOT_EXISTS.getMessage());
        }else if (Objects.equals("", password)) {
            map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
        }else if (!code.equalsIgnoreCase(captcha)) {
            map.put("status", ErrorEnum.CAPTCHA_ERROR.getStatus());
            map.put("message", ErrorEnum.CAPTCHA_ERROR.getMessage());
        }else if (!new BCryptPasswordEncoder().matches(password, userRepository.findByUsername(username).getPassword())) {
            map.put("status", ErrorEnum.USER_OR_PASSWORD_ERROR.getStatus());
            map.put("message", ErrorEnum.USER_OR_PASSWORD_ERROR.getMessage());
        }else {
            httpSession.setAttribute("user", userRepository.findByUsername(username));
            map.put("status", ErrorEnum.LOGIN_SUCCESS.getStatus());
            map.put("message", ErrorEnum.LOGIN_SUCCESS.getMessage());
        }
        httpSession.removeAttribute("captcha");
        return map;
    }
}
