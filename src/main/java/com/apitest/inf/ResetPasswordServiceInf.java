package com.apitest.inf;


import com.apitest.util.ServerResponse;

/**
 * @author huangshayang
 */
public interface ResetPasswordServiceInf {

    ServerResponse resetPasswordService(String newPassword, String captcha);
}
