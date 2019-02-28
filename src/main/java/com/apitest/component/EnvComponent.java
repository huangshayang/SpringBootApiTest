package com.apitest.component;

import com.apitest.entity.Enviroment;
import com.apitest.repository.EnvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class EnvComponent {

    private static EnvRepository envRepository;
    private static Enviroment enviroment;

    @Autowired
    public EnvComponent(EnvRepository envRepository) {
        EnvComponent.envRepository = envRepository;
    }

    public static Enviroment getEnviroment(int envId) {
        Optional<Enviroment> envOptional = envRepository.findById(envId);
        envOptional.ifPresent(env -> enviroment = env);
        return enviroment;
    }

}
