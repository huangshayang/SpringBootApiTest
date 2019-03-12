package com.apitest.api.impl;

import com.apitest.api.RegisterService;
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

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Log4j2
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RegisterServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse registerService(String username, String password, String token) {
        User user = new User();
        ServerResponse serverResponse;
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            if (isBlank(username) || isBlank(password)) {
                serverResponse = new ServerResponse(ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            } else if (userMapper.findUserByUsernameOrEmail(username, username) != null) {
                serverResponse = new ServerResponse(ErrorEnum.USER_IS_EXIST.getStatus(), ErrorEnum.USER_IS_EXIST.getMessage());
            } else if (redisTemplate.opsForValue().get(token) == null) {
                serverResponse = new ServerResponse(ErrorEnum.TOKEN_IS_ERROR.getStatus(), ErrorEnum.TOKEN_IS_ERROR.getMessage());
            } else {
                user.setUsername(username);
                user.setEmail(String.valueOf(redisTemplate.opsForValue().get(token)));
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userMapper.save(user);
                serverResponse = new ServerResponse(ErrorEnum.REGISTER_SUCCESS.getStatus(), ErrorEnum.REGISTER_SUCCESS.getMessage());
                redisTemplate.delete(token);
            }
            log.info("返回结果: " + serverResponse);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        return serverResponse;
    }
}
