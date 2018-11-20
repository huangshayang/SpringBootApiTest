package com.apitest.inf;


import javax.servlet.http.HttpServletRequest;

/**
 * @author huangshayang
 */
public interface MailSendServiceInf {
    Object resetPasswordMailService(HttpServletRequest request, String email);

    Object registerMailService(HttpServletRequest request, String email);
}
