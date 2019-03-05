package com.apitest.api;

import com.apitest.util.ServerResponse;

import javax.servlet.http.HttpServletRequest;

public interface MailSendService {
    ServerResponse resetPasswordMailService(HttpServletRequest request, String email);

    ServerResponse registerMailService(HttpServletRequest request, String email);
}
