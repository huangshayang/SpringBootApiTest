package com.apitest.security;

import com.apitest.annotation.Auth;
import com.apitest.error.ErrorEnum;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

import static com.apitest.configconsts.ConfigConsts.USERSESSION_KEY;

/**
 * @author huangshayang
 */
@Log4j2
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    /**
     * 表示是否要将当前的请求拦截下来，如果返货false请求被终止，如果为true请求会继续运行
     * @param request 请求
     * @param response 响应
     * @param object 表示的是被拦截的请求的目标对象
     * @return true or false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String reqCookie = request.getHeader("cookie");
        String referer = request.getHeader("referer");
        //获取访问地址
        String sitePart = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        log.info("request cookie: " + reqCookie);
        log.info("referer: " + referer);
        log.info("sitePart: " + sitePart);
        if (object instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod)object;
            Class type = handlerMethod.getBeanType();
            if (type.isAnnotationPresent(Auth.class)) {
                try {
                    if (reqCookie == null || reqCookie.isBlank() || reqCookie.isEmpty()) {
                        resToJson(response, new ServerResponse(ErrorEnum.AUTH_FAILED.getStatus(), ErrorEnum.AUTH_FAILED.getMessage()));
                        return false;
                    }else if (!Objects.equals(cookieToMap(reqCookie), request.getSession().getAttribute(USERSESSION_KEY))) {
                        resToJson(response, new ServerResponse(ErrorEnum.AUTH_FAILED.getStatus(), ErrorEnum.AUTH_FAILED.getMessage()));
                        return false;
                    }
                }catch (Exception e){
                    new ExceptionUtil(e);
                    return false;
                }
            }
        }
        Cookie resCookie = new Cookie(USERSESSION_KEY, cookieToMap(reqCookie));
        resCookie.setMaxAge(request.getSession().getMaxInactiveInterval());
        resCookie.setHttpOnly(true);
        resCookie.setPath("/");
        response.addCookie(resCookie);
        return true;
    }

    private void resToJson(HttpServletResponse response, ServerResponse serverResponse) {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        ObjectMapper jsonObject = new ObjectMapper();
        try {
            String json = jsonObject.writeValueAsString(serverResponse);
            OutputStream os = response.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
    }

    private String cookieToMap(String reqCookie){
        Map<String, String> map = new HashMap<>(8);
//        try {
            String[] str = reqCookie.split(";");
            for (String s : str) {
                String[] str2 = s.split("=");
                map.put(str2[0].trim(), str2[1]);
            }
//        }catch (ArrayIndexOutOfBoundsException e){
//            map.put("user_session", "");
//        }
        return map.get("user_session");
    }
}
