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
@RequestMapping(value = "/case", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class CaseController {

    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/api/{apiId}/all")
    public ServerResponse queryCaseByApiIdController(@PathVariable(name = "apiId") int apiId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return caseService.queryCaseByApiIdService(apiId, page, size);
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllCase(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return caseService.queryAllCaseService(page, size);
    }

    @PostMapping(value = "/api/{apiId}/add")
    public ServerResponse addCaseByApiIdController(@RequestBody Cases cases, @PathVariable(name = "apiId") int apiId){
        return caseService.addCaseByApiIdService(cases, apiId);
    }

    @DeleteMapping(value = "/api/{apiId}")
    public ServerResponse deleteAllCaseByApiIdController(@PathVariable(name = "apiId") int apiId){
        return caseService.deleteAllCaseByApiIdService(apiId);
    }

    @PutMapping(value = "/{id}")
    public ServerResponse modifyCaseController(@PathVariable(name = "id") int id, @RequestBody Cases cases){
        return caseService.modifyCaseService(id, cases);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteOneCaseController(@PathVariable(name = "id") int id){
        return caseService.deleteOneCaseService(id);
    }
}
