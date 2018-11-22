package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ResetPasswordServiceInf;
import com.apitest.repository.UserRepository;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class ResetPasswordService implements ResetPasswordServiceInf {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static ServerResponse serverResponse;

    @Autowired
    public ResetPasswordService(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate){
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ServerResponse resetPasswordService(String newPassword, String token) {
        try {
            log.info("参数: " + newPassword);
            if (newPassword == null || token == null || newPassword.getClass() != String.class) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (newPassword.isEmpty() || newPassword.isBlank()) {
                serverResponse = new ServerResponse(ErrorEnum.PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
            }else if (token.isBlank() || token.isEmpty()) {
                serverResponse = new ServerResponse(ErrorEnum.TOKEN_IS_EMPTY.getStatus(), ErrorEnum.TOKEN_IS_EMPTY.getMessage());
            }else if (!new BCryptPasswordEncoder().matches(String.valueOf(redisTemplate.opsForValue().get(token)), token)) {
                serverResponse = new ServerResponse(ErrorEnum.TOKEN_IS_ERROR.getStatus(), ErrorEnum.TOKEN_IS_ERROR.getMessage());
            }else {
                String username = String.valueOf(redisTemplate.opsForValue().get(token));
                User user = userRepository.findUserByUsernameOrEmail(username, username);
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                userRepository.saveAndFlush(user);
                serverResponse = new ServerResponse(ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus(), ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
                redisTemplate.delete(token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
