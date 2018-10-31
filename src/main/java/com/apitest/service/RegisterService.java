package com.apitest.service;


import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.RegisterServiceInf;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author huangshayang
 */
@Service
@Async
@Log4j2
public class RegisterService implements RegisterServiceInf {

    private final UserRepository userRepository;

    @Autowired
    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<Object> registerService(HttpSession httpSession, Map<String, String> models) {
        Map<String, Object> map = new HashMap<>(8);
        String username = models.get("username");
        String password = models.get("password");
        String captcha = models.get("captcha");
        String code = String.valueOf(httpSession.getAttribute("captcha"));
        log.warn("参数: " + models);
        User user = new User();
        try {
            if (username == null || password == null ||
                    username.getClass() != String.class ||
                    password.getClass() != String.class ||
                    captcha.getClass() != String.class) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (Objects.equals("", username) || Objects.equals("", password)){
                map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (userRepository.existsByUsername(username)) {
                map.put("status", ErrorEnum.USER_IS_EXIST.getStatus());
                map.put("message", ErrorEnum.USER_IS_EXIST.getMessage());
            }else if (!code.equalsIgnoreCase(captcha)) {
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
            log.error("错误信息: " + e);
            log.error("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }
        httpSession.removeAttribute("captcha");
        log.warn("返回结果: " + map);
        log.warn("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return CompletableFuture.completedFuture(map);
    }

}
