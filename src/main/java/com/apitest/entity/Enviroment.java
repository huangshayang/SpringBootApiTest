package com.apitest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class Enviroment implements Serializable {

    private Integer id;

    private String name;

    private String username;

    private String password;

    private String domain;

    @JsonIgnore
    private Long createTime;

    @JsonIgnore
    private Long updateTime;

    public Enviroment() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(System.currentTimeMillis() / 1000);
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(System.currentTimeMillis() / 1000);
        }
    }
}
