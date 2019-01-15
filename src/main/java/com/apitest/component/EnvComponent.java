package com.apitest.component;

import com.apitest.entity.Enviroment;
import com.apitest.repository.EnvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EnvComponent {

    private static EnvRepository envRepository;

    @Autowired
    public EnvComponent(EnvRepository envRepository) {
        EnvComponent.envRepository = envRepository;
    }

    public static Enviroment getEnviroment(int envId){
        return envRepository.findAll().get(envId - 1);
    }

}
