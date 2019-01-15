package com.apitest.quartz;

import com.apitest.component.RestCompoent;
import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.repository.CaseRepository;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class QuartzTask extends QuartzJobBean {

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        List<Apis> apisList = (List<Apis>) jobExecutionContext.getMergedJobDataMap().getWrappedMap().get("apisList");
        CaseRepository caseRepository = (CaseRepository) jobExecutionContext.getMergedJobDataMap().getWrappedMap().get("caseRepository");
        apisList.forEach(apis -> new Thread(() -> {
            lock.lock();
            List<Cases> casesList = caseRepository.findByApiId(apis.getId());
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
