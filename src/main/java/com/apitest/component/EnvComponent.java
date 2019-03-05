package com.apitest.component;

import com.apitest.entity.Enviroment;
import com.apitest.mapper.EnvMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;


@Component
public class EnvComponent {

    private static Enviroment enviroment;
    private static EnvComponent envComponent = new EnvComponent();

    @Resource
    private EnvMapper envMapper;

    public static Enviroment getEnviroment(int envId) {
        Optional<Enviroment> envOptional = envComponent.envMapper.findById(envId);
        envOptional.ifPresent(env -> enviroment = env);
        return enviroment;
    }

}
