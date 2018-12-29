package com.apitest.service;

import com.apitest.component.RestCompoent;
import com.apitest.entity.Apis;
import com.apitest.entity.Task;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.TaskServiceInf;
import com.apitest.repository.CaseRepository;
import com.apitest.repository.TaskRepository;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class TaskService implements TaskServiceInf {

    private final TaskRepository taskRepository;
    private final CaseRepository caseRepository;
    private final RestCompoent restCompoent;
    private static ServerResponse serverResponse;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static Map<Integer, ScheduledFuture> threadMap = new ConcurrentHashMap<>(20);

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

    @Autowired
    public TaskService(TaskRepository taskRepository, CaseRepository caseRepository, RestCompoent restCompoent) {
        this.taskRepository = taskRepository;
        this.caseRepository = caseRepository;
        this.restCompoent = restCompoent;
    }

    @Override
    public ServerResponse queryAllTaskService(){
        try {
            List<Task> tasks = taskRepository.findAll();
            serverResponse = new ServerResponse<>(ErrorEnum.TASK_QUERY_SUCCESS.getStatus(), ErrorEnum.TASK_QUERY_SUCCESS.getMessage(), tasks);
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse addTaskService(Task task){
        log.info("参数: " + task);
        try {
            if (isBlank(task.getName())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EMPTY.getStatus(), ErrorEnum.TASK_NAME_IS_EMPTY.getMessage());
            }else if (isBlank(task.getIsMultiThread().toString())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getStatus(), ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getMessage());
            }else if (task.getApisList().size() <= 0 || task.getApisList() == null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_APIS_IS_EMPTY.getStatus(), ErrorEnum.TASK_APIS_IS_EMPTY.getMessage());
            }else if (checkCron(task.getTaskTime())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_INVALID.getStatus(), ErrorEnum.TASK_TIME_IS_INVALID.getMessage());
            }else if (taskRepository.findByName(task.getName()) != null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EXIST.getStatus(), ErrorEnum.TASK_NAME_IS_EXIST.getMessage());
            }else {
                task.setTaskStatus(Task.TaskStatusEnum.CREATED);
                taskRepository.save(task);
                serverResponse = new ServerResponse(ErrorEnum.TASK_ADD_SUCCESS.getStatus(), ErrorEnum.TASK_ADD_SUCCESS.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse modifyTaskService(Task task, int id){
        log.info("参数: " + task);
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isPresent()) {
                Task taskByName = taskRepository.findByName(task.getName());
                if (isBlank(task.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EMPTY.getStatus(), ErrorEnum.TASK_NAME_IS_EMPTY.getMessage());
                }else if (isBlank(task.getTaskStatus().getStatus())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_STATUS_IS_EMPTY.getStatus(), ErrorEnum.TASK_STATUS_IS_EMPTY.getMessage());
                }else if (isBlank(task.getIsMultiThread().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getStatus(), ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getMessage());
                }else if (task.getApisList().size() <= 0 || task.getApisList() == null) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_APIS_IS_EMPTY.getStatus(), ErrorEnum.TASK_APIS_IS_EMPTY.getMessage());
                }else if (isBlank(task.getTaskTime())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_EMPTY.getStatus(), ErrorEnum.TASK_TIME_IS_EMPTY.getMessage());
                }else if (checkCron(task.getTaskTime())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_INVALID.getStatus(), ErrorEnum.TASK_TIME_IS_INVALID.getMessage());
                }else if (taskByName != null && taskByName.getId() != id) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EXIST.getStatus(), ErrorEnum.TASK_NAME_IS_EXIST.getMessage());
                }else {
                    Task task1 = taskOptional.get();
                    task1.setApisList(task.getApisList());
                    task1.setIsMultiThread(task.getIsMultiThread());
                    task1.setName(task.getName());
                    task1.setTaskStatus(task.getTaskStatus());
                    task1.setTaskTime(task.getTaskTime());
                    task1.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    taskRepository.saveAndFlush(task1);
                    serverResponse = new ServerResponse(ErrorEnum.TASK_ADD_SUCCESS.getStatus(), ErrorEnum.TASK_ADD_SUCCESS.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteTaskService(int id){
        try {
            if (taskRepository.findById(id).isPresent()) {
                taskRepository.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.TASK_DELETE_SUCCESS.getStatus(), ErrorEnum.TASK_DELETE_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse taskStartService(int id){
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isPresent()) {
                List<Apis> apisList = taskOptional.get().getApisList();
                if (threadMap.containsKey(id) && taskOptional.get().getTaskStatus().equals(Task.TaskStatusEnum.RUNNING)) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_RUNNING.getStatus(), ErrorEnum.TASK_FUTURE_IS_RUNNING.getMessage());
                }else {
                    ScheduledFuture future = threadPoolTaskScheduler.schedule(new TaskFuture(), new CronTrigger(taskOptional.get().getTaskTime()));
                    threadMap.putIfAbsent(id, future);
                    taskOptional.get().setTaskStatus(Task.TaskStatusEnum.RUNNING);
                    taskRepository.saveAndFlush(taskOptional.get());
                    serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_START_SUCCESS.getStatus(), ErrorEnum.TASK_FUTURE_START_SUCCESS.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    public ServerResponse testTask(int id){
        System.out.println(threadMap);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (threadMap.containsKey(id)) {
            if (threadMap.get(id).isCancelled() && taskOptional.get().getTaskStatus().getStatus().equals(Task.TaskStatusEnum.RUNNING.getStatus())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_PAUSED.getStatus(), ErrorEnum.TASK_FUTURE_IS_PAUSED.getMessage());
            }else {
                threadMap.get(id).cancel(true);
                taskOptional.get().setTaskStatus(Task.TaskStatusEnum.PAUSING);
                serverResponse = new ServerResponse(1,"暂停成功");
            }
        }
        return serverResponse;
    }

    @Override
    public ServerResponse taskPauseService(int id){
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isPresent()) {
                if (threadMap.containsKey(id)) {
                    if (threadMap.get(id).isCancelled() && taskOptional.get().getTaskStatus().equals(Task.TaskStatusEnum.PAUSING)) {
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_PAUSED.getStatus(), ErrorEnum.TASK_FUTURE_IS_PAUSED.getMessage());
                    }else {
                        threadMap.get(id).cancel(true);
                        taskOptional.get().setTaskStatus(Task.TaskStatusEnum.PAUSING);
                        taskRepository.saveAndFlush(taskOptional.get());
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_PAUSING_SUCCESS.getStatus(), ErrorEnum.TASK_FUTURE_PAUSING_SUCCESS.getMessage());
                    }
                }else {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_NULL.getStatus(), ErrorEnum.TASK_FUTURE_IS_NULL.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse taskStopService(int id){
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isPresent()) {
                if (threadMap.get(id) != null) {
                    if (threadMap.get(id).isCancelled()) {
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_PAUSED.getStatus(), ErrorEnum.TASK_FUTURE_IS_PAUSED.getMessage());
                    }else {
                        threadMap.get(id).cancel(true);
                        threadMap.remove(id);
                        taskOptional.get().setTaskStatus(Task.TaskStatusEnum.STOPPED);
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_STOPPED_SUCCESS.getStatus(), ErrorEnum.TASK_FUTURE_STOPPED_SUCCESS.getMessage());
                    }
                }else {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_NULL.getStatus(), ErrorEnum.TASK_FUTURE_IS_NULL.getMessage());
                }
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    private static boolean checkCron(String expression){
        try {
            CronScheduleBuilder.cronSchedule(expression);
        }catch (Exception e){
            return true;
        }
        return false;
    }

    private class TaskFuture implements Runnable{
        @Override
        public void run() {
            //                        apisList.forEach(apis -> new Thread(() -> {
//                            List<Cases> casesList = caseRepository.findByApiId(apis.getId());
//                            casesList.forEach(cases -> new Thread(() -> {
//                                if (cases.getAvailable()) {
//                                    restCompoent.taskApiCaseExecByLock(apis, cases);
//                                }
//                            }).start());
//                        }).start());
            System.out.println("测试");
        }
    }
}
