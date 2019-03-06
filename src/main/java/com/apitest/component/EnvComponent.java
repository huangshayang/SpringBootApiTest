package com.apitest.component;

import com.apitest.entity.Enviroment;
import com.apitest.mapper.EnvMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Optional;

@Component
public class EnvComponent {

    @Resource
    private EnvMapper envMapper;

    private static EnvComponent envComponent;

    @PostConstruct
    public void init() {
        envComponent = this;
        envComponent.envMapper = this.envMapper;
    }

    public static Enviroment getEnviroment(int envId) {
        Optional<Enviroment> envOptional = envComponent.envMapper.findById(envId);
        return envOptional.orElse(null);
    }
}
