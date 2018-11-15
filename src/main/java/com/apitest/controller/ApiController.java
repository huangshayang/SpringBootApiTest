package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.entity.Apis;
import com.apitest.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
@Auth
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = "/add")
    public CompletableFuture<Object> addApiController(@RequestBody Apis api){
        return CompletableFuture.completedFuture(apiService.addApiService(api));
    }

    @GetMapping(value = "/{id}")
    public CompletableFuture<Object> querySingleApiController(@PathVariable(name = "id") int id){
        return CompletableFuture.completedFuture(apiService.queryOneApiService(id));
    }

    @GetMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<Object> queryPageApiController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return CompletableFuture.completedFuture(apiService.queryPageApiService(page, size));
    }

    @PutMapping(value = "/{id}")
    public CompletableFuture<Object> modifyApiController(@PathVariable(name = "id") int id, @RequestBody Apis api){
        return CompletableFuture.completedFuture(apiService.modifyApiService(id, api));
    }

    @DeleteMapping(value = "/{id}")
    public CompletableFuture<Object> deleteApiController(@PathVariable(name = "id") int id){
        return CompletableFuture.completedFuture(apiService.deleteApiService(id));
    }

    @PostMapping(value = "/exec/{id}")
    public CompletableFuture<Object> execApiController(@PathVariable(name = "id") int id){
        return CompletableFuture.completedFuture(apiService.execApiService(id));
    }

    @PostMapping(value = "/exec/one/{id}")
    public CompletableFuture<Object> execApiOneController(@PathVariable int id){
        return CompletableFuture.completedFuture(apiService.execApiServiceOne(id));
    }
}
