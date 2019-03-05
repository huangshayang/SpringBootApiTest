package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.api.ApiService;
import com.apitest.entity.Apis;
import com.apitest.util.ServerResponse;
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
    public ServerResponse addApiController(@RequestBody Apis api) {
        return apiService.addApiService(api);
    }

    @GetMapping(value = "/{id}")
    public ServerResponse queryOneApiController(@PathVariable(name = "id") int id) {
        return apiService.queryOneApiService(id);
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllApiController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return apiService.queryAllApiService(page, size);
    }

    @PutMapping(value = "/{id}")
    public ServerResponse modifyApiController(@PathVariable(name = "id") int id, @RequestBody Apis api) {
        return apiService.modifyApiService(id, api);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteApiController(@PathVariable(name = "id") int id) {
        return apiService.deleteApiService(id);
    }
}
