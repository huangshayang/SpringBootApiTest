package com.apitest.service;

import com.apitest.entity.Apis;
import com.apitest.entity.Task;
import com.apitest.error.ErrorEnum;
import com.apitest.inf.TaskServiceInf;
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
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
@Log4j2
public class TaskService implements TaskServiceInf {

    private final TaskRepository taskRepository;
    private static ServerResponse serverResponse;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> future;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
            }else if (isBlank(task.getTaskStatus())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_STATUS_IS_EMPTY.getStatus(), ErrorEnum.TASK_STATUS_IS_EMPTY.getMessage());
            }else if (isBlank(task.getIsMultiThread().toString())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getStatus(), ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getMessage());
            }else if (task.getApisList().size() <= 0) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_APIS_IS_EMPTY.getStatus(), ErrorEnum.TASK_APIS_IS_EMPTY.getMessage());
            }else if (checkCron(task.getTaskTime())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_INVALID.getStatus(), ErrorEnum.TASK_TIME_IS_INVALID.getMessage());
            }else if (taskRepository.findByName(task.getName()) != null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EXIST.getStatus(), ErrorEnum.TASK_NAME_IS_EXIST.getMessage());
            }else {
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
                }else if (isBlank(task.getTaskStatus())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_STATUS_IS_EMPTY.getStatus(), ErrorEnum.TASK_STATUS_IS_EMPTY.getMessage());
                }else if (isBlank(task.getIsMultiThread().toString())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getStatus(), ErrorEnum.TASK_IsMultiThread_IS_EMPTY.getMessage());
                }else if (task.getApisList().size() <= 0) {
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
                future = threadPoolTaskScheduler.schedule(() -> {
                    if (future == null) {
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_NULL.getStatus(), ErrorEnum.TASK_FUTURE_NULL.getMessage());
                    }else if (future.isCancelled()) {
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_CANCELLED.getStatus(), ErrorEnum.TASK_FUTURE_IS_CANCELLED.getMessage());
                    }else if (future.isDone()) {
                        serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_DONE.getStatus(), ErrorEnum.TASK_FUTURE_IS_DONE.getMessage());
                    }else {
                        List<Apis> apisList = taskOptional.get().getApisList();
                        for (Apis apis : apisList) {
                            System.out.println(apis);
                        }
                    }
                }, triggerContext -> new CronTrigger(taskOptional.get().getTaskTime()).nextExecutionTime(triggerContext));
                serverResponse = new ServerResponse(ErrorEnum.TASK_START_SUCCESS.getStatus(), ErrorEnum.TASK_START_SUCCESS.getMessage());
            }else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        }catch (Exception e){
            new ExceptionUtil(e);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse taskStopService(int id){
        if (taskRepository.findById(id).isPresent()) {
            if (future == null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_NULL.getStatus(), ErrorEnum.TASK_FUTURE_NULL.getMessage());
            }else if (future.isDone()) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_DONE.getStatus(), ErrorEnum.TASK_FUTURE_IS_DONE.getMessage());
            }else if (future.isCancelled()) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_FUTURE_IS_CANCELLED.getStatus(), ErrorEnum.TASK_FUTURE_IS_CANCELLED.getMessage());
            }else if (threadPoolTaskScheduler.isDaemon()) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_ThreadPoolTaskScheduler_IS_DAEMON.getStatus(), ErrorEnum.TASK_ThreadPoolTaskScheduler_IS_DAEMON.getMessage());
            } else {
                future.cancel(true);
                threadPoolTaskScheduler.shutdown();
            }
            serverResponse = new ServerResponse(ErrorEnum.TASK_CANCEL_SUCCESS.getStatus(), ErrorEnum.TASK_CANCEL_SUCCESS.getMessage());
        }else {
            serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
        }
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
}
