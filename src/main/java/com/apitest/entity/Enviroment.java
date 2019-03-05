package com.apitest.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Enviroment implements Serializable {

    private Integer id;

    private String name;

    private String username;

    private String password;

    private String domain;

    private Timestamp createTime;

    private Timestamp updateTime;

    public Enviroment() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
