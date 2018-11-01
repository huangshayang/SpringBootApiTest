package com.apitest.log;

import lombok.extern.log4j.Log4j2;

import java.io.PrintWriter;
import java.io.StringWriter;

@Log4j2
public class ExceptionLog {
    public ExceptionLog(Exception e, Object o) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        log.error("错误信息: " + pw.toString());
        log.error("参数: " + o);
        log.error("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
    }
}
