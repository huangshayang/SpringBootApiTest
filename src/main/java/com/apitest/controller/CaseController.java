package com.apitest.controller;


import com.apitest.entity.Cases;
import com.apitest.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/api")
public class CaseController {

    private final CaseService caseService;

    @Autowired
    private CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/{apiId}/case")
    public Object queryCaseByApiIdController(HttpSession httpSession, @PathVariable int apiId){
        return caseService.queryCaseByApiIdService(httpSession, apiId);
    }

    @PostMapping(value = "/{apiId}/case/add")
    public Object addCaseByApiIdController(HttpSession httpSession, @RequestBody Cases cases, @PathVariable int apiId){
        return caseService.addCaseByApiIdService(httpSession, cases, apiId);
    }

    @DeleteMapping(value = "/{apiId}/case")
    public Object deleteAllCaseController(HttpSession httpSession, @PathVariable int apiId){
        return caseService.deleteAllCaseByApiIdService(httpSession, apiId);
    }

    @PutMapping(value = "/case/{id}")
    public Object modifyCaseController(HttpSession httpSession, @PathVariable int id, @RequestBody Cases cases){
        return caseService.modifyCaseService(httpSession, id, cases);
    }

    @GetMapping(value = "/case/{id}")
    public Object queryOneCaseController(HttpSession httpSession, @PathVariable int id){
        return caseService.queryOneCaseService(httpSession, id);
    }

    @DeleteMapping(value = "/case/{id}")
    public Object deleteOneCaseController(HttpSession httpSession, @PathVariable int id){
        return caseService.deleteOneCaseService(httpSession, id);
    }
}
