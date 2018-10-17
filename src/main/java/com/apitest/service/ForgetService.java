package com.apitest.service;

import com.apitest.entity.User;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.ForgetServiceInf;
import com.apitest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class ForgetService implements ForgetServiceInf {

    private final UserRepository userRepository;

    @Autowired
    private ForgetService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Object forgetPasswordService(HttpSession httpSession, Map<String, String> models) {
        Map<String, Object> map = new HashMap<>(8);
        String username = models.get("username");
        String newPassword = models.get("newPwd");
        String token = models.get("token");
        User user = userRepository.findByUsername(username);
        if (username == null || newPassword == null || token == null ||
                username.getClass() != String.class ||
                newPassword.getClass() != String.class ||
                token.getClass() != String.class) {
            map.put("status", ErrorEnum.PARAMETER_ERROR.getStatus());
            map.put("message", ErrorEnum.PARAMETER_ERROR.getMessage());
        }else if (Objects.equals("", username) || Objects.equals("", newPassword)) {
            map.put("status", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.USERNAME_OR_PASSWORD_IS_EMPTY.getMessage());
        }else if (!userRepository.existsByUsername(username)) {
            map.put("status", ErrorEnum.USER_IS_NOT_EXIST.getStatus());
            map.put("message", ErrorEnum.USER_IS_NOT_EXIST.getMessage());
        }else if (Objects.equals("", token)) {
            map.put("status", ErrorEnum.TOKEN_IS_EMPTY.getStatus());
            map.put("message", ErrorEnum.TOKEN_IS_EMPTY.getMessage());
        }else if (!Objects.equals(token, httpSession.getAttribute("token"))) {
            map.put("status", ErrorEnum.TOKEN_IS_ERROR.getStatus());
            map.put("message", ErrorEnum.TOKEN_IS_ERROR.getMessage());
        }else {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.saveAndFlush(user);
            map.put("status", ErrorEnum.RESET_PASSWORD_SUCCESS.getStatus());
            map.put("message", ErrorEnum.RESET_PASSWORD_SUCCESS.getMessage());
            httpSession.removeAttribute("token");
        }
        return map;
    }

    @Override
    public Object getTokenService(HttpSession httpSession) {
        Map<String, Object> map = new HashMap<>(8);
        String token = UUID.randomUUID().toString();
        //创建一个session key为token
        httpSession.setAttribute("token", token);
        map.put("status", ErrorEnum.TOKEN_SUSSCESS.getStatus());
        map.put("message", ErrorEnum.TOKEN_SUSSCESS.getMessage());
        map.put("data", httpSession.getAttribute("token"));
        return map;
    }
}
