package com.apitest.controller;


import com.apitest.annotation.Auth;
import com.apitest.entity.Cases;
import com.apitest.service.CaseService;
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
public class CaseController {

    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/{apiId}/case")
    public ServerResponse queryCaseByApiIdController(@PathVariable(name = "apiId") int apiId){
        return caseService.queryCaseByApiIdService(apiId);
    }

    @PostMapping(value = "/{apiId}/case/add")
    public ServerResponse addCaseByApiIdController(@RequestBody Cases cases, @PathVariable(name = "apiId") int apiId){
        return caseService.addCaseByApiIdService(cases, apiId);
    }

    @DeleteMapping(value = "/{apiId}/case")
    public ServerResponse deleteAllCaseController(@PathVariable(name = "apiId") int apiId){
        return caseService.deleteAllCaseByApiIdService(apiId);
    }

    @PutMapping(value = "/case/{id}")
    public ServerResponse modifyCaseController(@PathVariable(name = "id") int id, @RequestBody Cases cases){
        return caseService.modifyCaseService(id, cases);
    }

    @GetMapping(value = "/case/{id}")
    public ServerResponse queryOneCaseController(@PathVariable(name = "id") int id){
        return caseService.queryOneCaseService(id);
    }

    @DeleteMapping(value = "/case/{id}")
    public ServerResponse deleteOneCaseController(@PathVariable(name = "id") int id){
        return caseService.deleteOneCaseService(id);
    }
}
