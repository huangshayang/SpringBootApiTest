package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ForgetServiceInf;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Async
public class ForgetService implements ForgetServiceInf {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ForgetService(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate){
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CompletableFuture<Object> resetPasswordService(String newPassword, String code) {
        Map<String, Object> map = new HashMap<>(8);
        if (newPassword == null || code == null || newPassword.getClass() != String.class) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
        }else if (newPassword.isEmpty() || newPassword.isBlank()) {
            map.put("status", ErrorEnum.PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.PASSWORD_IS_EMPTY.getMessage());
        }else if (code.isBlank() || code.isEmpty()) {
            map.put("status", ErrorEnum.TOKEN_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.TOKEN_IS_EMPTY.getMessage());
        }else if (!forgetCodeCheck(code)) {
            map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
        }else {
            String username = String.valueOf(redisTemplate.boundHashOps("mail").get(code));
            User user = userRepository.findUserByUsernameOrEmail(username, username);
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.saveAndFlush(user);
            map.put("status", ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus());
            map.put("message", ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
            redisTemplate.delete("mail");
        }
        return CompletableFuture.completedFuture(map);
    }

    private boolean forgetCodeCheck(String code){
        boolean isMail = redisTemplate.hasKey("mail");
        boolean isResetCode = redisTemplate.boundHashOps("mail").hasKey("resetCode");
        if (isMail && isResetCode) {
            return Objects.equals(code, redisTemplate.boundHashOps("mail").get("resetCode"));
        }
        return false;
    }
}
