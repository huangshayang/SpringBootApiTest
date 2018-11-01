package com.apitest.service;

import com.apitest.error.ErrorEnum;
import com.apitest.inf.LoginServiceInf;
import com.apitest.log.ExceptionLog;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class LoginService implements LoginServiceInf {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<Object> loginService(HttpSession httpSession, Map<String, String> models){
        Map<String, Object> map = new HashMap<>(8);
        try {
            log.info("参数: " + models);
            String code = String.valueOf(httpSession.getAttribute("captcha"));
            log.info("验证码: " + code);
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
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            new ExceptionLog(e, models);
        }
        httpSession.removeAttribute("captcha");
        log.info(httpSession.getAttribute("captcha") == null);
        return CompletableFuture.completedFuture(map);
    }
}
