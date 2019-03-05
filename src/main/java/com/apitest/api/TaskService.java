package com.apitest.api;

import com.apitest.entity.Task;
import com.apitest.util.ServerResponse;

public interface TaskService {
    ServerResponse queryAllTaskService();

    ServerResponse queryOneTaskService(int id);

    ServerResponse addTaskService(Task task);

    ServerResponse modifyTaskService(Task task, int id);

    ServerResponse deleteTaskService(int id);

    ServerResponse taskStartService(int id);

    ServerResponse taskPauseService(int id);

    ServerResponse taskStopService(int id);

    ServerResponse taskResumeService(int id);
}
