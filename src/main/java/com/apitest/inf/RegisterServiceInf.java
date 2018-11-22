package com.apitest.inf;

import com.apitest.util.ServerResponse;


public interface RegisterServiceInf {
    ServerResponse registerService(String username, String password, String captcha);

}
