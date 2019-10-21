package com.apitest.controller;


import com.apitest.annotation.Auth;
import com.apitest.api.CaseService;
import com.apitest.entity.Cases;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author HSY
 */
@RestController
@RequestMapping(value = "/case", consumes = MediaType.APPLICATION_JSON_VALUE)
@Auth
public class CaseController {

    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllCaseController(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return caseService.queryAllCaseService(page, size);
    }

    @GetMapping(value = "/{id}")
    public ServerResponse queryOneCaseController(@PathVariable(name = "id") int id) {
        return caseService.queryOneCaseService(id);
    }

    @PostMapping(value = "/add")
    public ServerResponse addCaseController(@RequestBody Cases cases) {
        return caseService.addCaseService(cases);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteCaseController(@PathVariable(name = "id") int id) {
        return caseService.deleteCaseService(id);
    }

    @PutMapping(value = "/{id}")
    public ServerResponse modifyCaseController(@PathVariable(name = "id") int id, @RequestBody Cases cases) {
        return caseService.modifyCaseService(id, cases);
    }
}
