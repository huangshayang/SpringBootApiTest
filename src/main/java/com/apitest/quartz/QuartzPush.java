package com.apitest.quartz;

import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Log4j2
public class QuartzPush extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        pushExec(jobExecutionContext);
    }

    private void pushExec(JobExecutionContext jobExecutionContext) {

    }
}
