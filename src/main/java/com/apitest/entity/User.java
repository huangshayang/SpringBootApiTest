package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author huangshayang
 */
@Data
//必须实现序列化，才能存储到redis
public class User implements Serializable {

    @JsonIgnore
    private Integer id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Timestamp createTime;

    @JsonIgnore
    private Timestamp updateTime;

    public User() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
