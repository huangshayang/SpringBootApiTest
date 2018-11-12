package com.apitest.security;

import com.apitest.annotation.Auth;
import com.apitest.error.ErrorEnum;
import com.apitest.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws JsonProcessingException {
        String jwt = request.getHeader("auth");
        log.info(jwt);
        String payloadKey = "apitest";
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        Map<String, Object> map = new HashMap<>(8);
        if (method.isAnnotationPresent(Auth.class)) {
            try {
                if (jwt.getClass() == null || jwt.isEmpty() || jwt.isBlank() || !Objects.equals(payloadKey, JwtUtil.parseJWT(jwt).get("info", String.class))) {
                    map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                    map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
                }
            }catch (ExpiredJwtException | SignatureException | MalformedJwtException e){
                map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
            }catch (Exception e){
                e.printStackTrace();
            }
            returnJson(response, map);
            return false;
        }
        return true;
    }

    private void returnJson(HttpServletResponse response, Map<String, Object> map) throws JsonProcessingException {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        ObjectMapper jsonObject = new ObjectMapper();
        String json = jsonObject.writeValueAsString(map);

        try {
            OutputStream os = response.getOutputStream();
            os.write(json.getBytes());
        } catch (IOException e) {
            log.error("response error", e);
        }
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
