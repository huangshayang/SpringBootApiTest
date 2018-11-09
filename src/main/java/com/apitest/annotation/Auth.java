package com.apitest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 可用在方法名上
@Target({ElementType.METHOD})
// 运行时有效
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
}
