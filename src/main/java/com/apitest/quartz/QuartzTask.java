package com.apitest.quartz;

import com.apitest.component.RestCompoent;
import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.mapper.CaseMapper;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class QuartzTask extends QuartzJobBean {

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        taskExec(jobExecutionContext);
    }

    private void taskExec(JobExecutionContext jobExecutionContext) {
        List<Apis> apisList = (List<Apis>) jobExecutionContext.getMergedJobDataMap().getWrappedMap().get("apisList");
        CaseMapper caseMapper = (CaseMapper) jobExecutionContext.getMergedJobDataMap().getWrappedMap().get("caseMapper");
        apisList.forEach(apis -> new Thread(() -> {
            lock.lock();
            List<Cases> casesList = caseMapper.findByApiId(apis.getId());
            for (Cases cases : casesList) {
                new Thread(() -> {
                    //这里需要加锁，不然会报错
                    lock.lock();
                    if (cases.getAvailable()) {
                        RestCompoent.taskApiCaseExecByLock(apis, cases);
                    }
                    lock.unlock();
                }).start();
            }
            lock.unlock();
        }).start());
    }
}
