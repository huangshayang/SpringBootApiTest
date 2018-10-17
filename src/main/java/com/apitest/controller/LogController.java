package com.apitest.controller;


import com.apitest.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class LogController {

    private final LogService logService;

    @Autowired
    private LogController(LogService logService){
        this.logService = logService;
    }

    @GetMapping(value = "/{apiId}/log")
    public Object queryPageLogController(HttpSession httpSession, @PathVariable int apiId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size){
        return logService.queryPageLogByApiIdService(httpSession, apiId, page, size);
    }

    @DeleteMapping(value = "/{apiId}/log")
    public Object deleteAllLogByApiIdController(HttpSession httpSession, @PathVariable int apiId){
        return logService.deleteAllLogByApiIdService(httpSession, apiId);
    }

    @DeleteMapping(value = "/log/{id}")
    public Object deleteOneLogController(HttpSession httpSession, @PathVariable int id){
        return logService.deleteOneLogService(httpSession, id);
    }
}
