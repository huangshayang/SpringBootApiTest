package com.apitest.security;

import com.apitest.annotation.Auth;
import com.apitest.error.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author huangshayang
 */
@Log4j2
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    /**
     * 表示是否要将当前的请求拦截下来，如果返货false请求被终止，如果为true请求会继续运行
     * Object object表示的是被拦截的请求的目标对象
     * @param request
     * @param response
     * @param object
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws UnsupportedEncodingException {
//        String jwt = request.getHeader("auth");
        String reqCookie = request.getHeader("cookie");
//        String payloadKey = "apitest";
        Map<String, Object> map = new HashMap<>(8);
        if (!"REQUEST".equals(request.getDispatcherType().name())) {
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Class type = handlerMethod.getBeanType();
        if (type.isAnnotationPresent(Auth.class)) {
            try {
//                if (jwt == null || jwt.isEmpty() || jwt.isBlank() || !Objects.equals(payloadKey, JwtUtil.parseJWT(jwt).get("info", String.class))) {
//                    map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
//                    map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
//                    returnJson(response, map);
//                    return false;
//                }
                if (reqCookie == null || reqCookie.isBlank() || reqCookie.isEmpty() || !Objects.equals(cookieToMap(reqCookie), request.getSession().getAttribute("user_session"))) {
                    map.put("status", ErrorEnum.AUTH_FAILED.getStatus());
                    map.put("message", ErrorEnum.AUTH_FAILED.getMessage());
                    resToJson(response, map);
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Cookie resCookie = new Cookie("user_session", cookieToMap(reqCookie));
        resCookie.setMaxAge(request.getSession().getMaxInactiveInterval());
        resCookie.setHttpOnly(true);
        resCookie.setPath("/");
        response.addCookie(resCookie);
        return true;
    }

    private void resToJson(HttpServletResponse response, Map<String, Object> map) {
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

    private String cookieToMap(String reqCookie){
        Map<String, String> map = new HashMap<>(8);
        try {
            String[] str = reqCookie.split(";");
            for (String s : str) {
                String[] str2 = s.split("=");
                map.put(str2[0].trim(), str2[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            map.put("user_session", "");
        }
        return map.get("user_session");
    }
}
