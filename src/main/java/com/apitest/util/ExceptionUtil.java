package com.apitest.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionUtil {
    public ExceptionUtil(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass()).append(" ").append(e.getMessage()).append("\n");
        StackTraceElement[] stackTraceElement = e.getStackTrace();
        for (StackTraceElement traceElement : stackTraceElement) {
            sb.append("\tat ").append(traceElement).append("\n");
        }
        log.error("错误信息: " + sb.toString());
        log.error("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
    }
}
