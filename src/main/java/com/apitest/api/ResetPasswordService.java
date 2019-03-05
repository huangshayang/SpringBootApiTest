package com.apitest.api;

import com.apitest.util.ServerResponse;

public interface ResetPasswordService {
    ServerResponse resetPasswordService(String newPassword, String token);
}
