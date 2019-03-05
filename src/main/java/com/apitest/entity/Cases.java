package com.apitest.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Cases implements Serializable {

    private Integer id;

    private String jsonData;

    private String paramsData;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer apiId;

    private String name;

    private Boolean available;

    private Cases() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (jsonData == null) {
            this.jsonData = "";
        }
        if (paramsData == null) {
            this.paramsData = "";
        }
    }
}
