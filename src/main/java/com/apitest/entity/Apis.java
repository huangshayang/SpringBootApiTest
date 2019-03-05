package com.apitest.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Apis implements Serializable {

    private Integer id;

    private String url;

    private Timestamp createTime;

    private String method;

    private Integer envId;

    private Timestamp updateTime;

    private Boolean cookie;

    private String name;

    private Apis() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
