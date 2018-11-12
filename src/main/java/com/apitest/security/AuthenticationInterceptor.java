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

    /**
     * 表示是否要将当前的请求拦截下来，如果返货false请求被终止，如果为true请求会继续运行
     * Object object表示的是被拦截的请求的目标对象
     * @param request
     * @param response
     * @param object
     * @return
     */
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
                    return false;
                }
            }catch (ExpiredJwtException | SignatureException | MalformedJwtException e){
                map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
                returnJson(response, map);
                return false;
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
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
