package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.entity.Enviroment;
import com.apitest.service.EnvService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/env", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class EnvController {

    private final EnvService envService;

    @Autowired
    public EnvController(EnvService envService) {
        this.envService = envService;
    }

    @PostMapping(value = "/add")
    public ServerResponse addEnvController(@RequestBody Enviroment env) {
        return envService.addEnvService(env);
    }

    @PutMapping(value = "/{id}")
    public ServerResponse modifyEnvController(@RequestBody Enviroment env, @PathVariable(name = "id") int id) {
        return envService.modifyEnvService(env, id);
    }

    @GetMapping(path = "/{id}")
    public ServerResponse queryOneEnvController(@PathVariable(name = "id") int id){
        return envService.queryOneEnvService(id);
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllEnvController() {
        return envService.queryAllEnvService();
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteEnvController(@PathVariable(name = "id") int id) {
        return envService.deleteEnvService(id);
    }
}
