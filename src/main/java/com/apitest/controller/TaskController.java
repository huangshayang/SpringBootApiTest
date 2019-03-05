package com.apitest.controller;

import com.apitest.annotation.Auth;
import com.apitest.api.TaskService;
import com.apitest.entity.Task;
import com.apitest.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Auth
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/all")
    public ServerResponse queryAllTaskController() {
        return taskService.queryAllTaskService();
    }

    @GetMapping(value = "/{id}")
    public ServerResponse queryOneTaskController(@PathVariable(name = "id") int id) {
        return taskService.queryOneTaskService(id);
    }

    @PostMapping(value = "/add")
    public ServerResponse addTaskController(@RequestBody Task task) {
        return taskService.addTaskService(task);
    }

    @PutMapping(value = "/{id}")
    public ServerResponse modifyTaskController(@RequestBody Task task, @PathVariable(name = "id") int id) {
        return taskService.modifyTaskService(task, id);
    }

    @DeleteMapping(value = "/{id}")
    public ServerResponse deleteTaskController(@PathVariable(name = "id") int id) {
        return taskService.deleteTaskService(id);
    }

    @PostMapping(value = "/quartz/start/{id}")
    public ServerResponse taskStartController(@PathVariable(name = "id") int id) {
        return taskService.taskStartService(id);
    }

    @PostMapping(value = "/quartz/pause/{id}")
    public ServerResponse taskPauseController(@PathVariable(name = "id") int id) {
        return taskService.taskPauseService(id);
    }

    @PostMapping(value = "/quartz/stop/{id}")
    public ServerResponse taskStopController(@PathVariable(name = "id") int id) {
        return taskService.taskStopService(id);
    }

    @PostMapping(value = "/quartz/resume/{id}")
    public ServerResponse taskResumeController(@PathVariable(name = "id") int id) {
        return taskService.taskResumeService(id);
    }
}
