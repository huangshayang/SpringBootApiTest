package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class Apis implements Serializable {

    private Integer id;

    private String url;

    @JsonIgnore
    private Long createTime;

    private String method;

    private Integer envId;

    @JsonIgnore
    private Long updateTime;

    private Boolean cookie;

    private String name;

    private Apis() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(System.currentTimeMillis() / 1000);
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(System.currentTimeMillis() / 1000);
        }
    }

}
