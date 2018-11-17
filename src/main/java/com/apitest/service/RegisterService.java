package com.apitest.service;


import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.RegisterServiceInf;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RegisterService(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CompletableFuture<Object> registerService(String username, String password, String captcha) {
        Map<String, Object> map = new HashMap<>(8);
        User user = new User();
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            log.info("验证码" + captcha);
            if (username == null || password == null ||
                    password.getClass() != String.class) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()){
                map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (userRepository.findUserByUsernameOrEmail(username, username) != null) {
                map.put("status", ErrorEnum.USER_IS_EXIST.getStatus());
                map.put("message", ErrorEnum.USER_IS_EXIST.getMessage());
            }else if (!Objects.equals(captcha, String.valueOf(redisTemplate.boundHashOps("mail").get("registerCode")))) {
                map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
                map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
            }else {
                user.setUsername(username);
                user.setEmail(username);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.save(user);
                map.put("status", ErrorEnum.REGISTER_SUCCESS.getStatus());
                map.put("message", ErrorEnum.REGISTER_SUCCESS.getMessage());
                redisTemplate.delete("mail");
            }
            log.info("返回结果: " + map);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(map);
    }
}
