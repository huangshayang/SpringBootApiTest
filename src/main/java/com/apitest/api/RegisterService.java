package com.apitest.api;

import com.apitest.util.ServerResponse;

public interface RegisterService {
    ServerResponse registerService(String username, String password, String token);
}
