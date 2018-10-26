package com.apitest.controller;


import com.apitest.entity.Cases;
import com.apitest.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class CaseController {

    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/{apiId}/case")
    public CompletableFuture<Object> queryCaseByApiIdController(HttpSession httpSession, @PathVariable int apiId){
        return CompletableFuture.completedFuture(caseService.queryCaseByApiIdService(httpSession, apiId));
    }

    @PostMapping(value = "/{apiId}/case/add")
    public CompletableFuture<Object> addCaseByApiIdController(HttpSession httpSession, @RequestBody Cases cases, @PathVariable int apiId){
        return CompletableFuture.completedFuture(caseService.addCaseByApiIdService(httpSession, cases, apiId));
    }

    @DeleteMapping(value = "/{apiId}/case")
    public CompletableFuture<Object> deleteAllCaseController(HttpSession httpSession, @PathVariable int apiId){
        return CompletableFuture.completedFuture(caseService.deleteAllCaseByApiIdService(httpSession, apiId));
    }

    @PutMapping(value = "/case/{id}")
    public CompletableFuture<Object> modifyCaseController(HttpSession httpSession, @PathVariable int id, @RequestBody Cases cases){
        return CompletableFuture.completedFuture(caseService.modifyCaseService(httpSession, id, cases));
    }

    @GetMapping(value = "/case/{id}")
    public CompletableFuture<Object> queryOneCaseController(HttpSession httpSession, @PathVariable int id){
        return CompletableFuture.completedFuture(caseService.queryOneCaseService(httpSession, id));
    }

    @DeleteMapping(value = "/case/{id}")
    public CompletableFuture<Object> deleteOneCaseController(HttpSession httpSession, @PathVariable int id){
        return CompletableFuture.completedFuture(caseService.deleteOneCaseService(httpSession, id));
    }
}
