package com.apitest.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Task implements Serializable {

    private Integer id;

    private String name;

    private String taskTime;

    private String apiIdList;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Task() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
