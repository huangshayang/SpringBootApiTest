package com.apitest.controller;


import com.apitest.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService){
        this.logService = logService;
    }

    @GetMapping(value = "/{apiId}/log")
    public CompletableFuture<Object> queryPageLogController(HttpSession httpSession, @PathVariable int apiId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size){
        return CompletableFuture.completedFuture(logService.queryPageLogByApiIdService(httpSession, apiId, page, size));
    }

    @DeleteMapping(value = "/{apiId}/log")
    public CompletableFuture<Object> deleteAllLogByApiIdController(HttpSession httpSession, @PathVariable int apiId){
        return CompletableFuture.completedFuture(logService.deleteAllLogByApiIdService(httpSession, apiId));
    }

    @DeleteMapping(value = "/log/{id}")
    public CompletableFuture<Object> deleteOneLogController(HttpSession httpSession, @PathVariable int id){
        return CompletableFuture.completedFuture(logService.deleteOneLogService(httpSession, id));
    }
}
