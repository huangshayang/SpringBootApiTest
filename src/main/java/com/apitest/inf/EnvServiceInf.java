package com.apitest.inf;


import com.apitest.entity.Enviroment;
import com.apitest.util.ServerResponse;

public interface EnvServiceInf {
    ServerResponse addEnvService(Enviroment env);

    ServerResponse modifyEnvService(Enviroment env, int id);

    ServerResponse queryAllEnvService();

    ServerResponse deleteEnvService(int id);
}
