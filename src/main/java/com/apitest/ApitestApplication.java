package com.apitest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.apitest.mapper")
public class ApitestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApitestApplication.class, args);
    }
}
