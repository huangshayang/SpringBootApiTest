package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.entity.Apis;
import com.apitest.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = "/add")
    public CompletableFuture<Object> addApiController(HttpServletRequest request, @RequestBody Apis api){
        return CompletableFuture.completedFuture(apiService.addApiService(request, api));
    }

    @GetMapping(value = "/{id}")
    public CompletableFuture<Object> querySingleApiController(HttpServletRequest request, @PathVariable int id){
        return CompletableFuture.completedFuture(apiService.queryOneApiService(request, id));
    }

    @Auth
    @GetMapping(value = "/all")
    public CompletableFuture<Object> queryPageApiController(HttpServletRequest request, @RequestParam (value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size){
        return CompletableFuture.completedFuture(apiService.queryPageApiService(request, page, size));
    }

    @PutMapping(value = "/{id}")
    public CompletableFuture<Object> modifyApiController(HttpServletRequest request, @PathVariable int id, @RequestBody Apis api){
        return CompletableFuture.completedFuture(apiService.modifyApiService(request, id, api));
    }

    @DeleteMapping(value = "/{id}")
    public CompletableFuture<Object> deleteApiController(HttpServletRequest request, @PathVariable int id){
        return CompletableFuture.completedFuture(apiService.deleteApiService(request, id));
    }

    @PostMapping(value = "/exec/{id}")
    public CompletableFuture<Object> execApiController(HttpSession httpSession, @PathVariable int id){
        return CompletableFuture.completedFuture(apiService.execApiService(httpSession, id));
    }

    @PostMapping(value = "/exec/one/{id}")
    public CompletableFuture<Object> execApiOneController(HttpSession httpSession, @PathVariable int id){
        return CompletableFuture.completedFuture(apiService.execApiServiceOne(httpSession, id));
    }
}
