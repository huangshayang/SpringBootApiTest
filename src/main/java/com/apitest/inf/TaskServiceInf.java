package com.apitest.inf;

import com.apitest.entity.Task;
import com.apitest.util.ServerResponse;

public interface TaskServiceInf {
    ServerResponse queryAllTaskService();

    ServerResponse addTaskService(Task task);

    ServerResponse modifyTaskService(Task task, int id);

    ServerResponse deleteTaskService(int id);

    ServerResponse taskStartService(int id);

    ServerResponse taskStopService(int id);
}
