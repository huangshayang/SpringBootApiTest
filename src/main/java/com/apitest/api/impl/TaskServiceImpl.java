package com.apitest.api.impl;

import com.apitest.api.TaskService;
import com.apitest.entity.Apis;
import com.apitest.entity.Task;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.CaseMapper;
import com.apitest.mapper.TaskMapper;
import com.apitest.quartz.QuartzTask;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
@Log4j2
public class TaskServiceImpl implements TaskService {

    private static ServerResponse serverResponse;
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private static Scheduler scheduler;
    private static JobDataMap jobDataMap;

    static {
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            jobDataMap = new JobDataMap();
        } catch (SchedulerException e) {
            new ExceptionUtil(e);
        }
    }

    private static Map<Integer, Map.Entry<JobDetail, CronTrigger>> jobMap = new ConcurrentHashMap<>(20);

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private CaseMapper caseMapper;

    @Override
    public ServerResponse queryAllTaskService() {
        try {
            List<Task> tasks = taskMapper.findAllTask();
            serverResponse = new ServerResponse<>(ErrorEnum.TASK_QUERY_SUCCESS.getStatus(), ErrorEnum.TASK_QUERY_SUCCESS.getMessage(), tasks);
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryOneTaskService(int id) {
        try {
            Optional<Task> task = taskMapper.findById(id);
            serverResponse = task.map(task1 -> new ServerResponse<>(ErrorEnum.TASK_QUERY_SUCCESS.getStatus(), ErrorEnum.TASK_QUERY_SUCCESS.getMessage(), task1)).orElseGet(() -> new ServerResponse<>(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage()));
        }catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse addTaskService(Task task) {
        log.info("参数: " + task);
        try {
            if (isBlank(task.getName())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EMPTY.getStatus(), ErrorEnum.TASK_NAME_IS_EMPTY.getMessage());
            } else if (task.getApisList().size() <= 0 || task.getApisList() == null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_APIS_IS_EMPTY.getStatus(), ErrorEnum.TASK_APIS_IS_EMPTY.getMessage());
            } else if (checkCron(task.getTaskTime())) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_INVALID.getStatus(), ErrorEnum.TASK_TIME_IS_INVALID.getMessage());
            } else if (taskMapper.findByName(task.getName()) != null) {
                serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EXIST.getStatus(), ErrorEnum.TASK_NAME_IS_EXIST.getMessage());
            } else {
                taskMapper.save(task);
                serverResponse = new ServerResponse(ErrorEnum.TASK_ADD_SUCCESS.getStatus(), ErrorEnum.TASK_ADD_SUCCESS.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse modifyTaskService(Task task, int id) {
        log.info("参数: " + task);
        try {
            Optional<Task> taskOptional = taskMapper.findById(id);
            if (taskOptional.isPresent()) {
                Task taskByName = taskMapper.findByName(task.getName());
                if (isBlank(task.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EMPTY.getStatus(), ErrorEnum.TASK_NAME_IS_EMPTY.getMessage());
                } else if (task.getApisList().size() <= 0 || task.getApisList() == null) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_APIS_IS_EMPTY.getStatus(), ErrorEnum.TASK_APIS_IS_EMPTY.getMessage());
                } else if (isBlank(task.getTaskTime())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_EMPTY.getStatus(), ErrorEnum.TASK_TIME_IS_EMPTY.getMessage());
                } else if (checkCron(task.getTaskTime())) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_TIME_IS_INVALID.getStatus(), ErrorEnum.TASK_TIME_IS_INVALID.getMessage());
                } else if (taskByName != null && taskByName.getId() != id) {
                    serverResponse = new ServerResponse(ErrorEnum.TASK_NAME_IS_EXIST.getStatus(), ErrorEnum.TASK_NAME_IS_EXIST.getMessage());
                } else {
                    Task task1 = taskOptional.get();
                    task1.setApisList(task.getApisList());
                    task1.setName(task.getName());
                    task1.setTaskTime(task.getTaskTime());
                    task1.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    taskMapper.update(task1, id);
                    serverResponse = new ServerResponse(ErrorEnum.TASK_ADD_SUCCESS.getStatus(), ErrorEnum.TASK_ADD_SUCCESS.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteTaskService(int id) {
        try {
            if (taskMapper.findById(id).isPresent()) {
                taskMapper.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.TASK_DELETE_SUCCESS.getStatus(), ErrorEnum.TASK_DELETE_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse taskStartService(int id) {
        try {
            Optional<Task> taskOptional = taskMapper.findById(id);
            if (taskOptional.isPresent()) {
                boolean key = jobMap.containsKey(id);
//                Trigger.TriggerState triggerState = scheduler.getTriggerState(jobMap.get(id).getValue().getKey());
                if (key && scheduler.getTriggerState(jobMap.get(id).getValue().getKey()) == Trigger.TriggerState.NORMAL) {
                    serverResponse = new ServerResponse(ErrorEnum.QUATZ_IS_RUNNING.getStatus(), ErrorEnum.QUATZ_IS_RUNNING.getMessage());
                } else if (key && scheduler.getTriggerState(jobMap.get(id).getValue().getKey()) == Trigger.TriggerState.PAUSED) {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_IS_PAUSED.getStatus(), ErrorEnum.QUARTZ_IS_PAUSED.getMessage());
                } else {
                    List<Apis> apisList = taskOptional.get().getApisList();
                    jobDataMap.putIfAbsent("apisList", apisList);
                    jobDataMap.putIfAbsent("caseMapper", caseMapper);
                    JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).setJobData(jobDataMap).withIdentity(taskOptional.get().getName()).build();
                    CronTrigger trigger = TriggerBuilder.newTrigger()
                            .startAt(DateBuilder.evenSecondDate(new Date()))
                            .withIdentity(taskOptional.get().getName())
                            .withSchedule(CronScheduleBuilder.cronSchedule(taskOptional.get().getTaskTime()).withMisfireHandlingInstructionDoNothing())
                            .build();
                    scheduler.scheduleJob(jobDetail, trigger);
                    jobMap.putIfAbsent(id, Map.entry(jobDetail, trigger));
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_START_SUCCESS.getStatus(), ErrorEnum.QUARTZ_START_SUCCESS.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    public void testResume(int id) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobMap.get(id).getValue().getKey().getName());
        CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (oldTrigger != null) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobMap.get(id).getValue().getCronExpression()).withMisfireHandlingInstructionDoNothing();
            oldTrigger = oldTrigger.getTriggerBuilder().startAt(DateBuilder.evenSecondDate(new Date())).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, oldTrigger);
        } else {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobMap.get(id).getValue().getCronExpression()).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().startAt(DateBuilder.evenSecondDate(new Date())).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).withIdentity(jobMap.get(id).getKey().getKey()).build();
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);
            scheduler.scheduleJob(jobDetail, triggerSet, true);
        }
    }

    @Override
    public ServerResponse taskPauseService(int id) {
        try {
            Optional<Task> taskOptional = taskMapper.findById(id);
            if (taskOptional.isPresent()) {
                boolean key = jobMap.containsKey(id);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(jobMap.get(id).getValue().getKey());
                if (key && triggerState == Trigger.TriggerState.NORMAL) {
                    scheduler.pauseTrigger(jobMap.get(id).getValue().getKey());
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_STOPPED_SUCCESS.getStatus(), ErrorEnum.QUARTZ_STOPPED_SUCCESS.getMessage());
                } else if (key && triggerState == Trigger.TriggerState.PAUSED) {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_IS_PAUSED.getStatus(), ErrorEnum.QUARTZ_IS_PAUSED.getMessage());
                } else if (key && triggerState == Trigger.TriggerState.NONE) {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_IS_NONE.getStatus(), ErrorEnum.QUARTZ_IS_NONE.getMessage());
                } else {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_UNKNOWN_ERROR.getStatus(), ErrorEnum.QUARTZ_UNKNOWN_ERROR.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse taskStopService(int id) {
        try {
            Optional<Task> taskOptional = taskMapper.findById(id);
            if (taskOptional.isPresent()) {
                boolean key = jobMap.containsKey(id);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(jobMap.get(id).getValue().getKey());
                if (key && triggerState == Trigger.TriggerState.NONE) {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_IS_NONE.getStatus(), ErrorEnum.QUARTZ_IS_NONE.getMessage());
                } else if (key && (triggerState == Trigger.TriggerState.NORMAL || triggerState == Trigger.TriggerState.PAUSED)) {
                    scheduler.unscheduleJob(jobMap.get(id).getValue().getKey());
                    scheduler.deleteJob(jobMap.get(id).getKey().getKey());
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_STOPPED_SUCCESS.getStatus(), ErrorEnum.QUARTZ_STOPPED_SUCCESS.getMessage());
                } else {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_UNKNOWN_ERROR.getStatus(), ErrorEnum.QUARTZ_UNKNOWN_ERROR.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse taskResumeService(int id) {
        try {
            Optional<Task> taskOptional = taskMapper.findById(id);
            if (taskOptional.isPresent()) {
                boolean key = jobMap.containsKey(id);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(jobMap.get(id).getValue().getKey());
                if (key && triggerState == Trigger.TriggerState.NORMAL) {
                    serverResponse = new ServerResponse(ErrorEnum.QUATZ_IS_RUNNING.getStatus(), ErrorEnum.QUATZ_IS_RUNNING.getMessage());
                } else if (key && triggerState == Trigger.TriggerState.NONE) {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_IS_NONE.getStatus(), ErrorEnum.QUARTZ_IS_NONE.getMessage());
                } else if (key && triggerState == Trigger.TriggerState.PAUSED) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(jobMap.get(id).getKey().getKey().getName());
                    CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    String jobName = jobMap.get(id).getKey().getKey().getName();
                    String cron = jobMap.get(id).getValue().getCronExpression();
                    if (oldTrigger != null) {
                        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
                        oldTrigger = oldTrigger.getTriggerBuilder().startAt(DateBuilder.evenSecondDate(new Date())).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
                        scheduler.rescheduleJob(triggerKey, oldTrigger);
                    } else {
                        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
                        CronTrigger cronTrigger = TriggerBuilder.newTrigger().startAt(DateBuilder.evenSecondDate(new Date())).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
                        JobKey jobKey = new JobKey(jobName);
                        JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).withIdentity(jobKey).build();
                        HashSet<Trigger> triggerSet = new HashSet<>();
                        triggerSet.add(cronTrigger);
                        scheduler.scheduleJob(jobDetail, triggerSet, true);
                    }
                } else {
                    serverResponse = new ServerResponse(ErrorEnum.QUARTZ_UNKNOWN_ERROR.getStatus(), ErrorEnum.QUARTZ_UNKNOWN_ERROR.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.TASK_IS_NULL.getStatus(), ErrorEnum.TASK_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    private static boolean checkCron(String expression) {
        try {
            CronScheduleBuilder.cronSchedule(expression);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}