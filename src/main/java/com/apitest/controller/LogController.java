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
@RequestMapping(value = "/log", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService){
        this.logService = logService;
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllLogController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return logService.queryAllLogService(page, size);
    }

    @GetMapping(value = "/api/{apiId}/all")
    public ServerResponse queryAllLogByApiIdController(@PathVariable(name = "apiId") int apiId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return logService.queryAllLogByApiIdService(apiId, page, size);
    }

    @DeleteMapping(value = "/")
    public ServerResponse deleteAllLogController(){
        return logService.deleteAllLogService();
    }

    @DeleteMapping(value = "/api/{apiId}")
    public ServerResponse deleteAllLogByApiIdController(@PathVariable(name = "apiId") int apiId){
        return logService.deleteAllLogByApiIdService(apiId);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteOneLogController(@PathVariable(name = "id") int id){
        return logService.deleteOneLogService(id);
    }
}
