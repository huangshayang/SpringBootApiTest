package com.apitest.inf;


import com.apitest.util.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangshayang
 */
public interface MailSendServiceInf {
    ServerResponse resetPasswordMailService(HttpServletRequest request, String email);

    ServerResponse registerMailService(HttpServletRequest request, String email);
}
