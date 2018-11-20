package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ResetPasswordServiceInf;
import com.apitest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class ResetPasswordService implements ResetPasswordServiceInf {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ResetPasswordService(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate){
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object resetPasswordService(String newPassword, String token) {
        Map<String, Object> map = new HashMap<>(8);
        try {
            log.info("参数: " + newPassword);
            if (newPassword == null || token == null || newPassword.getClass() != String.class) {
                map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
                map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (newPassword.isEmpty() || newPassword.isBlank()) {
                map.put("status", ErrorEnum.PASSWORD_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
            }else if (token.isBlank() || token.isEmpty()) {
                map.put("status", ErrorEnum.TOKEN_IS_EMPTY.getStatus());
                map.put("message", ErrorEnum.TOKEN_IS_EMPTY.getMessage());
            }else if (!Objects.equals(token, String.valueOf(redisTemplate.boundHashOps("mail").get("resetToken")))) {
                map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
                map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
            }else {
                String username = String.valueOf(redisTemplate.boundHashOps("mail").get(token));
                User user = userRepository.findUserByUsernameOrEmail(username, username);
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                userRepository.saveAndFlush(user);
                map.put("status", ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus());
                map.put("message", ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
                redisTemplate.delete("mail");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + map);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return map;
    }
}
