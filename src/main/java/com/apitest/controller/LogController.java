package com.apitest.controller;


import com.apitest.annotation.Auth;
import com.apitest.service.LogService;
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
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService){
        this.logService = logService;
    }

    @GetMapping(value = "/{apiId}/log")
    public ServerResponse queryPageLogController(@PathVariable(name = "apiId") int apiId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return logService.queryPageLogByApiIdService(apiId, page, size);
    }

    @DeleteMapping(value = "/{apiId}/log")
    public ServerResponse deleteAllLogByApiIdController(@PathVariable(name = "apiId") int apiId){
        return logService.deleteAllLogByApiIdService(apiId);
    }

    @DeleteMapping(value = "/log/{id}")
    public ServerResponse deleteOneLogController(@PathVariable(name = "id") int id){
        return logService.deleteOneLogService(id);
    }
}
