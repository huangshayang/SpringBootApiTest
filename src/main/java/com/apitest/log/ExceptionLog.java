package com.apitest.log;

import lombok.extern.log4j.Log4j2;
import reactor.util.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

@Log4j2
public class ExceptionLog {
    public ExceptionLog(Exception e, @Nullable Object... o) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        log.error("错误信息: " + pw.toString());
        if (o != null) {
            for (Object o1:
                 o) {
                log.error("参数: " + o1);
            }
        }
        log.error("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
    }
}
