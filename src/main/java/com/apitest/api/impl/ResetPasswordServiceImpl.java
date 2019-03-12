package com.apitest.api.impl;

import com.apitest.api.ResetPasswordService;
import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.UserMapper;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Log4j2
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ResetPasswordServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse resetPasswordService(String newPassword, String token) {
        ServerResponse serverResponse;
        try {
            log.info("参数: " + newPassword);
            if (isBlank(newPassword)) {
                serverResponse = new ServerResponse(ErrorEnum.PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
            } else if (redisTemplate.opsForValue().get(token) == null) {
                serverResponse = new ServerResponse(ErrorEnum.TOKEN_IS_ERROR.getStatus(), ErrorEnum.TOKEN_IS_ERROR.getMessage());
            } else {
                String username = String.valueOf(redisTemplate.opsForValue().get(token));
                User user = userMapper.findUserByUsernameOrEmail(username, username);
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                userMapper.update(user, user.getId());
                serverResponse = new ServerResponse(ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus(), ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
                redisTemplate.delete(token);
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
