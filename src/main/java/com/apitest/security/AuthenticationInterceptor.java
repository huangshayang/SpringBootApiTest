package com.apitest.security;

import com.apitest.annotation.Auth;
import com.apitest.error.ErrorEnum;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.RevertUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Log4j2
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Value("${spring.USERSESSION_KEY}")
    private String usersessionKey;

    /**
     * 表示是否要将当前的请求拦截下来，如果返货false请求被终止，如果为true请求会继续运行
     *
     * @param request  请求
     * @param response 响应
     * @param object   表示的是被拦截的请求的目标对象
     * @return true or false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String reqCookie = request.getHeader("cookie");
        if (object instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) object;
            Class type = handlerMethod.getBeanType();
            if (type.isAnnotationPresent(Auth.class)) {
                try {
                    if (isBlank(reqCookie)) {
                        RevertUtil.resToJson(response, new ServerResponse(ErrorEnum.AUTH_FAILED.getStatus(), ErrorEnum.AUTH_FAILED.getMessage()));
                        return false;
                    } else if (request.getSession().getAttribute(RevertUtil.cookieToMap(reqCookie)) == null) {
                        RevertUtil.resToJson(response, new ServerResponse(ErrorEnum.AUTH_FAILED.getStatus(), ErrorEnum.AUTH_FAILED.getMessage()));
                        return false;
                    }
                } catch (Exception e) {
                    new ExceptionUtil(e);
                    return false;
                }
            }
        }
        Cookie resCookie = new Cookie(usersessionKey, RevertUtil.cookieToMap(reqCookie));
        resCookie.setMaxAge(request.getSession().getMaxInactiveInterval());
        resCookie.setHttpOnly(true);
        resCookie.setPath("/");
        response.addCookie(resCookie);
        return true;
    }
}
