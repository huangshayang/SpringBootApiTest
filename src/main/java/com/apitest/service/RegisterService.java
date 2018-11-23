package com.apitest.service;


import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.RegisterServiceInf;
import com.apitest.repository.UserRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * @author huangshayang
 */
@Service
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
    public ServerResponse registerService(String username, String password, String token) {
        User user = new User();
        ServerResponse serverResponse;
        try {
            log.info("用户名: " + username);
            log.info("密码: " + password);
            if (username == null || password == null || password.getClass() != String.class) {
                serverResponse = new ServerResponse(ErrorEnum.PARAMETER_ERROR.getStatus(), ErrorEnum.PARAMETER_ERROR.getMessage());
            }else if (username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()){
                serverResponse = new ServerResponse(ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
            }else if (userRepository.findUserByUsernameOrEmail(username, username) != null) {
                serverResponse = new ServerResponse(ErrorEnum.USER_IS_EXIST.getStatus(), ErrorEnum.USER_IS_EXIST.getMessage());
            }else if (redisTemplate.opsForValue().get(token) == null) {
                serverResponse = new ServerResponse(ErrorEnum.TOKEN_IS_ERROR.getStatus(), ErrorEnum.TOKEN_IS_ERROR.getMessage());
            }else {
                user.setUsername(username);
                user.setEmail(String.valueOf(redisTemplate.opsForValue().get(token)));
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.save(user);
                serverResponse = new ServerResponse(ErrorEnum.REGISTER_SUCCESS.getStatus(), ErrorEnum.REGISTER_SUCCESS.getMessage());
                redisTemplate.delete(token);
            }
            log.info("返回结果: " + serverResponse);
            log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        return serverResponse;
    }
}
