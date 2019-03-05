package com.apitest.api;

import com.apitest.entity.Enviroment;
import com.apitest.util.ServerResponse;

public interface EnvService {
    ServerResponse addEnvService(Enviroment env);

    ServerResponse modifyEnvService(Enviroment env, int id);

    ServerResponse queryOneEnvService(int id);

    ServerResponse queryAllEnvService();

    ServerResponse deleteEnvService(int id);
}
