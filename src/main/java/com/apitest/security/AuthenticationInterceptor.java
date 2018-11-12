package com.apitest.security;

import com.apitest.annotation.Auth;
import com.apitest.error.ErrorEnum;
import com.apitest.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huangshayang
 */
@Log4j2
public class AuthenticationInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String jwt = request.getHeader("auth");
        String payloadKey = "apitest";
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        Map<String, Object> map = new HashMap<>(8);
        if (method.isAnnotationPresent(Auth.class)) {
            try {
                if (jwt == null || jwt.isEmpty() || jwt.isBlank() || !Objects.equals(payloadKey, JwtUtil.parseJWT(jwt).get("info", String.class))) {
                    map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                    map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
                    returnJson(response, map);
                }
            }catch (ExpiredJwtException | SignatureException | MalformedJwtException e){
                map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
                returnJson(response, map);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }


    private void returnJson(HttpServletResponse response, Map<String, Object> map) {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        ObjectMapper jsonObject = new ObjectMapper();
        try {
            String json = jsonObject.writeValueAsString(map);
            OutputStream os = response.getOutputStream();
            os.write(json.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
