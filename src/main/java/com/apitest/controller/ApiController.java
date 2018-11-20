package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.entity.Apis;
import com.apitest.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = "/add")
    public Object addApiController(@RequestBody Apis api){
        return apiService.addApiService(api);
    }

    @GetMapping(value = "/{id}")
    public Object querySingleApiController(@PathVariable(name = "id") int id){
        return apiService.queryOneApiService(id);
    }

    @GetMapping(value = "/all")
    public Object queryPageApiController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return apiService.queryPageApiService(page, size);
    }

    @PutMapping(value = "/{id}")
    public Object modifyApiController(@PathVariable(name = "id") int id, @RequestBody Apis api){
        return apiService.modifyApiService(id, api);
    }

    @DeleteMapping(value = "/{id}")
    public Object deleteApiController(@PathVariable(name = "id") int id){
        return apiService.deleteApiService(id);
    }

    @PostMapping(value = "/exec/{id}")
    public Object execApiController(@PathVariable(name = "id") int id){
        return apiService.execApiService(id);
    }

    @PostMapping(value = "/exec/one/{id}")
    public Object execApiOneController(@PathVariable int id){
        return apiService.execApiServiceOne(id);
    }
}
