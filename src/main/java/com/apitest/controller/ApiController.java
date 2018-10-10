package com.apitest.controller;

import com.apitest.entity.Apis;
import com.apitest.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    private ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = "/add")
    public Callable<Object> addApiController(HttpSession httpSession, @RequestBody Apis api){
        return () -> apiService.addApiService(httpSession, api);
    }

    @GetMapping(value = "/{id}")
    public Callable<Object> querySingleApiController(HttpSession httpSession, @PathVariable int id){
        return () -> apiService.queryOneApiService(httpSession, id);
    }

    @GetMapping(value = "/all")
    public Callable<Object> queryPageApiController(HttpSession httpSession, @RequestParam (value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size){
        return () -> apiService.queryPageApiService(httpSession, page, size);
    }

    @PutMapping(value = "/{id}")
    public Callable<Object> modifyApiController(HttpSession httpSession, @PathVariable int id, @RequestBody Apis api){
        return () -> apiService.modifyApiService(httpSession, id, api);
    }

    @DeleteMapping(value = "/{id}")
    public Callable<Object> deleteApiController(HttpSession httpSession, @PathVariable int id){
        return () -> apiService.deleteApiService(httpSession, id);
    }

    @PostMapping(value = "/exec/{id}")
    public Callable<Object> execApiController(HttpSession httpSession, @PathVariable int id){
        return () -> apiService.execApiService(httpSession, id);
    }

    @PostMapping(value = "/exec/one/{id}")
    public Callable<Object> execApiOneController(HttpSession httpSession, @PathVariable int id){
        return () -> apiService.execApiServiceOne(httpSession, id);
    }
}
