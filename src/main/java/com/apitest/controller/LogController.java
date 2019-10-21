package com.apitest.controller;


import com.apitest.annotation.Auth;
import com.apitest.api.LogService;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/log", consumes = MediaType.APPLICATION_JSON_VALUE)
@Auth
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllLogController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return logService.queryAllLogService(page, size);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteLogController(@PathVariable(name = "id") int id) {
        return logService.deleteLogService(id);
    }

    @GetMapping(path = "/search")
    public ServerResponse querySearchLogController(@RequestParam(name = "startTime") long startTime, @RequestParam(name = "endTime") long endTime) {
        return logService.querySearchLogService(startTime, endTime);
    }
}
