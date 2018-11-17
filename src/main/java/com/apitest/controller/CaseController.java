package com.apitest.controller;


import com.apitest.annotation.Auth;
import com.apitest.entity.Cases;
import com.apitest.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = "auth")
@Auth
public class CaseController {

    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/{apiId}/case")
    public CompletableFuture<Object> queryCaseByApiIdController(HttpSession httpSession, @PathVariable(name = "apiId") int apiId){
        return caseService.queryCaseByApiIdService(httpSession, apiId);
    }

    @PostMapping(value = "/{apiId}/case/add")
    public CompletableFuture<Object> addCaseByApiIdController(HttpSession httpSession, @RequestBody Cases cases, @PathVariable(name = "apiId") int apiId){
        return caseService.addCaseByApiIdService(httpSession, cases, apiId);
    }

    @DeleteMapping(value = "/{apiId}/case")
    public CompletableFuture<Object> deleteAllCaseController(HttpSession httpSession, @PathVariable(name = "apiId") int apiId){
        return caseService.deleteAllCaseByApiIdService(httpSession, apiId);
    }

    @PutMapping(value = "/case/{id}")
    public CompletableFuture<Object> modifyCaseController(HttpSession httpSession, @PathVariable(name = "id") int id, @RequestBody Cases cases){
        return caseService.modifyCaseService(httpSession, id, cases);
    }

    @GetMapping(value = "/case/{id}")
    public CompletableFuture<Object> queryOneCaseController(HttpSession httpSession, @PathVariable(name = "id") int id){
        return caseService.queryOneCaseService(httpSession, id);
    }

    @DeleteMapping(value = "/case/{id}")
    public CompletableFuture<Object> deleteOneCaseController(HttpSession httpSession, @PathVariable(name = "id") int id){
        return caseService.deleteOneCaseService(httpSession, id);
    }
}
