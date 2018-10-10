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
public class RegisterService {

    private final UserRepository userRepository;

    @Autowired
    private RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Callable<Object> registerService(HttpSession httpSession, Map<String, Object> models) {
        Map<String, Object> map = new HashMap<>(8);
        String username = String.valueOf(models.get("username"));
        String password = String.valueOf(models.get("password"));
        String captcha = String.valueOf(models.get("captcha"));
        String code = String.valueOf(httpSession.getAttribute("number"));
        User user = new User();
        try {
            if (username == null || password == null) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (Objects.equals("", username) || Objects.equals("", password)){
                map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (userRepository.existsByUsername(username)) {
                map.put("status", ErrorEnum.USER_IS_EXIST.getStatus());
                map.put("message", ErrorEnum.USER_IS_EXIST.getMessage());
            }else if (code == null || !code.equalsIgnoreCase(captcha)) {
                map.put("status", ErrorEnum.CAPTCHA_ERROR.getStatus());
                map.put("message", ErrorEnum.CAPTCHA_ERROR.getMessage());
            }else {
                user.setUsername(username);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.save(user);
                map.put("status", ErrorEnum.REGISTER_SUCCESS.getStatus());
                map.put("message", ErrorEnum.REGISTER_SUCCESS.getMessage());
            }
        }catch (Exception e){
            map.put("status", ErrorEnum.USER_IS_EXIST.getStatus());
            map.put("message", ErrorEnum.USER_IS_EXIST.getMessage());
        }
        httpSession.invalidate();
        return () -> map;
    }

}
