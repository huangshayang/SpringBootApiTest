package com.apitest.component;

import com.apitest.entity.Enviroment;
import com.apitest.repository.EnvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EnvComponent {

    private final EnvRepository envRepository;
    private static String domain;
    private static String username;
    private static String password;

    @Autowired
    public EnvComponent(EnvRepository envRepository) {
        this.envRepository = envRepository;
    }

    public String getDomain(int envId){
        Optional<Enviroment> envOptional = envRepository.findById(envId);
        envOptional.ifPresent(env -> domain = env.getDomain());
        return domain;
    }

    public String getUsername(int envId){
        Optional<Enviroment> envOptional = envRepository.findById(envId);
        envOptional.ifPresent(env -> username = env.getUsername());
        return username;
    }

    public String getPassword(int envId){
        Optional<Enviroment> envOptional = envRepository.findById(envId);
        envOptional.ifPresent(env -> password = env.getPassword());
        return password;
    }
}
